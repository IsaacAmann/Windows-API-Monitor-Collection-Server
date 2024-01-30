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
	public int call_socket; //hooked
	public int call_bind; //hooked
	public int call_listen; //hooked
	public int call_accept; //hooked
	public int call_connect; //hooked
	public int call_send; //hooked
	public int call_recv; //Hooked
	public int call_InternetOpen; //Hooked
	public int call_InternetOpenUrl; //Hooked
	public int call_InternetReadFie; //Hooked
	public int call_InternetWriteFile; //Hooked
	//Service creation
	public int call_OpenSCManager; //hooked
	public int call_CreateService; //hooked
	public int call_StartServiceCtrlDispatcher; //hooked

	//File access calls
	public int call_CreateFile; //Hooked
	public int call_WriteFile; //Hooked
	public int call_CreateFileMapping; //Hooked
	public int call_GetWindowsDirectory; //Hooked
	public int call_SetFileTime; //Hooked

	//Registry access calls
	public int call_RegCreateKeyEx; //Hooked
	public int call_RegOpenKeyEx; //Hooked
	public int call_RegSetValueEx; //Hooked
	public int call_RegDeleteKeyEx; //Hooked
	public int call_RegGetValue; //Hooked

	//Memory calls
	public int call_VirtualAlloc; //Hooked
	public int call_VirtualProtect; //Hooked
	//Process access calls
	public int call_ReadProcessMemory; //Hooked
	public int call_WriteProcessMemory; //Hooked
	public int call_CreateRemoteThread; //Hooked
	public int call_QueueUserAPC; //Hooked
	public int call_ConnectNamedPipe; //Hooked
	public int call_CreateNamedPipe; //Hooked
	public int call_EnumProcesses;
	public int call_enumProcessModules;
	public int call_GetProcAddress;
	public int call_GetModulesfilename;
	public int call_GetModuleHandle;
	public int call_NtQueryInformationProcess;
	public int call_PeekNamedPipe;
	public int call_RegisterHotKey;
	public int call_GetCurrentProcessId; //Hooked
	public int call_OpenProcess; //Hooked
	public int call_TerminateProcess;

	//Threads / execution
	public int call_CreateProcess;
	public int call_ShellExecute;
	public int call_WinExec;
	//Spying
	public int call_GetAsyncKeyState;
	public int call_SetWindowsHookEx;
	public int call_GetForeGroundWindow;
	public int call_GetDC;
	public int call_BitBlt;
	public int call_gethostname; //hooked
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
}
