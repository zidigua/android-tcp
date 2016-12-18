#include <jni.h>
#include <string>
#include <stdlib.h>
#include <sys/socket.h>
#include <sys/epoll.h>
#include <netinet/in.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <android/log.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/epoll.h>
#include <arpa/inet.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <unistd.h>
#include <fcntl.h>
#include <pthread.h>
#include <signal.h>
#include <time.h>
#include <errno.h>

#include "native-lib.h"


typedef unsigned char u8;
typedef unsigned short u16;
typedef unsigned int u32;
typedef char s8;
typedef short s16;
typedef int s32;


#define LOG_TAG "NATIVE-LIB"


#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

extern "C" {

int sockfd = 0;
int server_epfd = 0;


JavaVM *javaVm = NULL;
jclass remotedesktopclass = NULL;

char g_send_data[100] = "&1|60C5A86385B3|3.0.1|110\r\n";


jint tcp_socket_close(int *psockfd) {
    LOGI("%s, %d ", __FUNCTION__, __LINE__);

    int ret = 0;
    int sockfd;
    if (psockfd == NULL) {
        return 0;
    }
    sockfd = *psockfd;
    if (sockfd == 0) {
        return 0;
    }
    if (sockfd > 0) {
        ret = close(sockfd);
        if (ret != 0) {
            perror("socket close");
        } else {
            *psockfd = 0;
            return 0;
        }
    }
    return 1;
}

jint tcp_socket_nonblock_set(int sock) {
    LOGI("%s, %d ", __FUNCTION__, __LINE__);

    int opts;
    opts = fcntl(sock, F_GETFL);
    if (opts < 0) {
        perror("fcntl F_GETFL");
        return -1;
    }
    opts = opts | O_NONBLOCK;
    if (fcntl(sock, F_SETFL, opts) < 0) {
        perror("fcntl F_SETFL");
        return -1;
    }
    return 0;
}

char *host_name_to_ip(char *hostname) {
    struct hostent *h = NULL;
    if ((h = gethostbyname(hostname)) == NULL) {
        LOGE("不能得到IP ");
        exit(1);
    }
    LOGI("HostName :%s ", h->h_name);
    char *ip = inet_ntoa(*((struct in_addr *) h->h_addr));
    LOGI("HostName :%s ", ip);
    return ip;
}


void Java_com_threepapa_vmtcp_app_MyApp_tcpClientConn(JNIEnv *env, jobject instance,
                                                      jboolean isText, jstring mac) {
    LOGI("%s, %d ", __FUNCTION__, __LINE__);


    //java String对象转换到本地字串
    const char *str = env->GetStringUTFChars(mac, NULL);
    if (NULL == str) return;

    memset(g_send_data, 0, sizeof(g_send_data));
    memcpy(g_send_data, str, strlen(str));

    //释放指向utf-8格式的char*的指针
    env->ReleaseStringUTFChars(mac, str);

    char *ip;
    s16 port;

    if (isText) {
        ip = (char *) "112.126.73.73";
        port = 9001;
    } else {
        ip = host_name_to_ip((char *) "sangebaba.com");
        port = 9417;
    }


    char server_ip[INET_ADDRSTRLEN] = "";
    memcpy(server_ip, ip, INET_ADDRSTRLEN);
    int err_log = 0;

    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0) {
        LOGE("socket %s", strerror(errno));
        return;
    }
    struct sockaddr_in server_addr;
    bzero(&server_addr, sizeof(server_addr));
    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(port);
    inet_pton(AF_INET, server_ip, &server_addr.sin_addr);
    usleep(1000 * 500);
    err_log = connect(sockfd, (struct sockaddr *) &server_addr, sizeof(server_addr));
    if (err_log != 0) {
        LOGE("connect %s", strerror(errno));
        tcp_socket_close(&sockfd);
        return;
    }
    if (tcp_socket_nonblock_set(sockfd) == -1) {
        LOGE("fcntl %s", strerror(errno));
        tcp_socket_close(&sockfd);
        return;
    }

    server_epfd = epoll_create(SOCKET_SET);
    tcp_socket_nonblock_set(server_epfd);

    struct epoll_event ev;
    ev.events = EPOLLIN | EPOLLOUT | EPOLLET;
    err_log = epoll_ctl(server_epfd, EPOLL_CTL_ADD, sockfd, &ev);
    if (err_log == -1) {
        LOGE("epoll_ctl %s", strerror(errno));
    }

}

void Java_com_threepapa_vmtcp_app_MyApp_tcpClientDisconn(JNIEnv *env, jobject instance) {
    tcp_socket_close(&sockfd);
}

int tcp_send_data(s8 *data, size_t source_len) {
    LOGI("%s, %d ", __FUNCTION__, __LINE__);

    send_again:
    ssize_t retval_len = write(sockfd, data, source_len);

#if 1
    LOGI("retval_len %li errno %s", retval_len, strerror(errno));
#endif

    if (retval_len < 0 && (errno == 4 || errno == 11)) {
        goto send_again;
    }
    else if (retval_len < 0 && errno == 104)        /*ECONNRESET  */    {
        return -2;
    }
    else if (retval_len > 0) {
        LOGI("----------------->");
        LOGI("    tcp_send_len %li data %s", retval_len, data);
        LOGI("<-----------------");
#if 0
        __android_log_print(ANDROID_LOG_INFO, LOG_TAG, "send_len < %li > data : ", retval_len);
        for (int j = 0; j < retval_len; j++) {
            __android_log_print(ANDROID_LOG_INFO, LOG_TAG, "%#x ", data[j]);
        }
#endif
        return (int) retval_len;
    }

    return (int) retval_len;
}


int tcp_recv_data(int sockfd) {
    LOGI("%s, %d ", __FUNCTION__, __LINE__);

    recv_again:
    s8 data[1024] = "";
    memset(data, 0, 1024);
    ssize_t retval_len = read(sockfd, data, 1024);

#if 1
    LOGI("retval_len %li errno %s", retval_len, strerror(errno));
#endif


    if (retval_len < 0 && (errno == 4 || errno == 11))    /*(errno == EINTR || errno == EAGAIN)*/{
        goto recv_again;
    } else if (retval_len < 0 && (errno == 104))            /*ECONNRESET  */{
        return -2;
    } else if (retval_len > 0) {
        LOGI("----------------->");
        LOGI("    tcp_recv_len %li data %s", retval_len, data);
        LOGI("<-----------------");

        JavaVMAttachArgs args = {JNI_VERSION_1_6, __FUNCTION__, __null};
        JNIEnv *jniEvn = __null;
        int res = javaVm->AttachCurrentThread(&jniEvn, &args);
        if (res < 0) {
            LOGE("AttachCurrentThread");
            return NULL;
        }
        jmethodID mId;
        if (jniEvn != NULL) {
            mId = jniEvn->GetStaticMethodID(remotedesktopclass, "onRecvData",
                                            "(Ljava/lang/String;)V");
        }
        if (mId == 0) {
            LOGE("find method4 error");
            return -1;
        }
        jstring jValue = jniEvn->NewStringUTF(data);
        jniEvn->CallStaticVoidMethod(remotedesktopclass, mId, jValue);

#if 0
        __android_log_print(ANDROID_LOG_INFO, LOG_TAG, "recv_len < %li > data : ", retval_len);
        for (int j = 0; j < retval_len; j++) {
            __android_log_print(ANDROID_LOG_INFO, LOG_TAG, "%#x ", data[j]);
        }
#endif
    }

    return (jint) retval_len;
}

#define SOCKET_SET 256

void *tcp_client_epoll(void *arg) {
    LOGI("%s, %d ", __FUNCTION__, __LINE__);

    while (1) {
        int nfds;
        struct epoll_event events[64];
        nfds = epoll_wait(server_epfd, events, 64, 500);
        if (nfds < 0) {
            LOGI("epoll_wait server_epfd %s", strerror(errno));
            continue;
        }
        for (int i = 0; i < nfds; ++i) {
            if (events[i].events & EPOLLIN) {
                tcp_recv_data(sockfd);
            } else if (events[i].events & EPOLLOUT) {
                tcp_send_data(g_send_data, strlen(g_send_data));
            }
        }
    }
}


void Java_com_threepapa_vmtcp_app_MyApp_sendData(JNIEnv *env, jobject instance, jstring prompt) {
    LOGI("%s, %d ", __FUNCTION__, __LINE__);

    //java String对象转换到本地字串
    const char *str = env->GetStringUTFChars(prompt, NULL);
    if (NULL == str) return;

    memset(g_send_data, 0, sizeof(g_send_data));
    memcpy(g_send_data, str, strlen(str));

    //释放指向utf-8格式的char*的指针
    env->ReleaseStringUTFChars(prompt, str);

    struct epoll_event ev;
    ev.events = EPOLLIN | EPOLLOUT | EPOLLET;
    epoll_ctl(server_epfd, EPOLL_CTL_MOD, sockfd, &ev);

}

void Java_com_threepapa_vmtcp_app_MyApp_setOnRecvDataListener(JNIEnv *env, jobject instance,
                                                              jobject l) {
    LOGI("%s, %d ", __FUNCTION__, __LINE__);

    pthread_t tid_tcp_lison;
    /*tcp客户端链路检测连接线程(主叫)*/
    int retval = pthread_create(&tid_tcp_lison, NULL, tcp_client_epoll, NULL);
    if (retval != 0) {
        LOGE("pthread_create tid_tcp_select %s",
             strerror(errno));
        return;
    }

    env->GetJavaVM(&javaVm);
    jclass native_class = env->GetObjectClass(l);
    remotedesktopclass = (jclass) env->NewGlobalRef(native_class);
}


};