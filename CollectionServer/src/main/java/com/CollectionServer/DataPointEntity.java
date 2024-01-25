package com.CollectionServer;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class DataPointEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer Id;
	
	public String executablePath;

	/*
	Monitored API calls include some selected from the following sources:
	https://book.hacktricks.xyz/reversing-and-exploiting/common-api-used-in-malware
	https://gist.github.com/404NetworkError/a81591849f5b6b5fe09f517efc189c1d

	List will also include other common Win32 API calls used by normal programs as well
	*/

	//Networking calls
	public int call_socket;
	public int call_bind;
	public int call_listen;
	public int call_accept;
	public int call_connect;
	public int call_send;
	public int call_recv;
	public int call_read;
	public int call_WSAStratup;
	public int call_InternetOpen;
	public int call_InternetOpenUrl;
	public int call_InternetReadFie;
	public int call_InternetWriteFile;
	public int call_URLDownloadToFile;

	//Service creation
	public int call_OpenSCManager;
	public int call_CreateService;
	public int call_StartServiceCtrlDispatcher;

	//File access calls
	public int call_CreateFile;
	public int call_CreateFileMapping;
	public int call_GetWindowsDirectory;
	public int call_SetFileTime;
	public int call_SfcTerminateWatcherThread;

	//Registry access calls
	public int call_RegCreateKeyEx;
	public int call_RegOpenKeyEx;
	public int call_RegSetValueEx;
	public int call_RegDeleteKeyEx;
	public int call_RegGetValue;

	//Encryption calls
	public int call_WinCrypt;
	public int call_CryptAcquireContext;
	public int call_CryptGenKey;
	public int call_CryptDeriveKey;
	public int call_CryptDecrypt;
	public int call_CryptReleaseContext;
	//Memory calls
	public int call_VirtualAlloc;
	public int call_VirtualProtect;
	//Process access calls
	public int call_ReadProcessMemory;
	public int call_WriteProcessMemory;
	public int call_CreateRemoteThread;
	public int call_QueueUserAPC;
	public int call_ZwUnmapViewOfSection;
	public int call_ConnectNamedPipe;
	public int call_CreateNamedPipe;
	public int call_EnumProcesses;
	public int call_enumProcessModules;
	public int call_GetProcAddress;
	public int call_GetModulesfilename;
	public int call_GetModuleHandle;
	public int call_NtQueryInformationProcess;
	public int call_PeekNamedPipe;
	public int call_RegisterHotKey;
	public int call_GetCurrentProcessId;
	public int call_OpenProcess;
	public int call_TerminateProcess;

	//Threads / execution
	public int call_CreateProcess;
	public int call_ShellExecute;
	public int call_WinExec;
	public int call_ResumeThread;
	public int call_SuspendThread;
	public int call_NtResumeThread;
	//Spying
	public int call_GetAsyncKeyState;
	public int call_SetWindowsHookEx;
	public int call_GetForeGroundWindow;
	public int call_GetDC;
	public int call_BitBlt;
	public int call_gethostname;
	public int call_GetKeyState;
	public int call_GetAdaptersInfo;
	public int call_isNTAdmin;
	public int call_LsaEnumerateLogonSessions;
	public int call_MapVirtualKey;
	public int call_NetShareEnum;
	public int call_SamlConnect;
	public int call_SamlGetPrivateData;
	public int call_SamQueryInformationUse;

	//Other
	public int call_IsDebuggerPresent;
	public int call_CreateMutex;
	public int call_LdrLoadDll;
	public int call_LoadResource;
}
