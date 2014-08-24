#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include "xwindowtools.h"
#include <X11/Xlib.h>
#include <X11/Xatom.h>

/*
 * Case insensitive strstr(). Used by window_search to allow case insensitive search
 * Works by copying cs and ct into buffers s1 & s2 and making both buffers lower case
 * Then returns strstr(s1, s2)
 */
char *stristr(const char *cs, const char *ct)
{
	int i;
	char s1[strlen(cs)];
	char s2[strlen(ct)];
	strcpy(s1, cs);
	strcpy(s2, ct);
	
	for(i = 0; i < strlen(cs); i++)
		s1[i] = tolower(s1[i]);
	s1[i] = '\0';
	
	for(i = 0; i < strlen(ct); i++)
		s2[i] = tolower(s2[i]);
	s2[i] = '\0';
	
	return strstr(s1, s2);
}

/*
 * Returns the title of the given window
 */
char *window_name(Display *disp, Window window)
{
        Atom props = XInternAtom(disp, "WM_NAME", False), type;
        int form;
        unsigned long remain, len;
        unsigned char *list;

        XGetWindowProperty(disp, window, props, 0, 1024, False, AnyPropertyType,
                                &type, &form, &len, &remain, &list);
        return (char*)list;
}

/*
 * Returns the id of a window which contains the substring search in its title
 * (case insensitive) or -1 if no window is found
 */
JNIEXPORT jint JNICALL Java_com_joc_jguii_Application_windowSearch(JNIEnv *env, jobject obj, jstring str)
/*int window_search(const char *search)*/
{
	Display *disp = XOpenDisplay(NULL);
	Window *wlist;
	Atom props = XInternAtom(disp, "_NET_CLIENT_LIST", False), type;
	int form, i, id;
	unsigned long remain, len;
	unsigned char *list;

	const char *search = (*env)->GetStringUTFChars(env, str, 0);
	
	
	XGetWindowProperty(disp, XDefaultRootWindow(disp), props, 0, 1024, False, XA_WINDOW,
				&type, &form, &len, &remain, &list);
	wlist = (Window *)list;

	for(i = 0; i < (int)len; i++){
		if(wlist[i] != 0){
			if(stristr(window_name(disp, wlist[i]), search) != NULL){
				id = (int)wlist[i];
				XFree((char*)wlist);
				XCloseDisplay(disp);
				(*env)->ReleaseStringUTFChars(env, str, search);
				return id;
			}
		}
	}
	XFree((char*)wlist);
	XCloseDisplay(disp);
	(*env)->ReleaseStringUTFChars(env, str, search);
	return -1;
}

JNIEXPORT jint JNICALL Java_com_joc_jguii_ApplicationHandler_windowSearch(JNIEnv *env, jobject obj, jstring str)
/*int window_search(const char *search)*/
{
        Display *disp = XOpenDisplay(NULL);
        Window *wlist;
        Atom props = XInternAtom(disp, "_NET_CLIENT_LIST", False), type;
        int form, i, id;
        unsigned long remain, len;
        unsigned char *list;

        const char *search = (*env)->GetStringUTFChars(env, str, 0);

        
        XGetWindowProperty(disp, XDefaultRootWindow(disp), props, 0, 1024, False, XA_WINDOW,
                                &type, &form, &len, &remain, &list);
        wlist = (Window *)list;

        for(i = 0; i < (int)len; i++){
                if(wlist[i] != 0){
                        if(stristr(window_name(disp, wlist[i]), search) != NULL){
                                id = (int)wlist[i];
                                XFree((char*)wlist);
                                XCloseDisplay(disp);
                                (*env)->ReleaseStringUTFChars(env, str, search);
                                return id;
                        }
                }
	}
        XFree((char*)wlist);
        XCloseDisplay(disp);
        (*env)->ReleaseStringUTFChars(env, str, search);
        return -1;
}

/*
 * Sets the window with id "id" as the focused window and then
 * raises it to the top
 */
JNIEXPORT void JNICALL Java_com_joc_jguii_Application_setWindowFocus(JNIEnv *env, jobject obj, jint id)
/*void set_window_focus(int id)*/
{
	Display *disp = XOpenDisplay(NULL);
	Window window = (Window)id;
	XSetInputFocus(disp, window, RevertToParent, CurrentTime);
	XRaiseWindow(disp, window);
	XCloseDisplay(disp);
	
	return;
}

/*
 * Returns the id of the currently focued window
 */
JNIEXPORT jint JNICALL Java_com_joc_jguii_ApplicationHandler_getWindowFocus(JNIEnv *env, jobject obj)
/*int get_window_focus(void)*/
{
	Display *disp = XOpenDisplay(NULL);
	Window *window;
	int revert, form;
	Atom props = XInternAtom(disp, "_NET_ACTIVE_WINDOW", False), type;
	unsigned long remain, len;
	unsigned char *list;
	
	XGetWindowProperty(disp, XDefaultRootWindow(disp), props, 0, 1024, False, XA_WINDOW, 
				&type, &form, &len, &remain, &list);
	window = (Window *)list;
	
	return (int)*window;
}

int main(void){}
