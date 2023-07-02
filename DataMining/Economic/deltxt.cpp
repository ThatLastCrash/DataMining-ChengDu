#include<iostream>
#include<stdlib.h>
#include<stdio.h>
#include<fstream>
#include<string>
using namespace std;
int main()
{
	ifstream in("apriori.txt");
	ofstream out("apriori_result.txt");
	string s="";
	while(getline(in,s))
	{
		int index1=s.find("¾ÛÀà±àºÅ=")+9;
		int index2=s.find("Àà±ğ=")+5;
		int num1=0;
		string str="";
		for(int i=index1;i<s.length();i++)
		{
			if(s[i]==' ')break;
			num1=num1*10+s[i]-'0';
		}
		str=s.substr(index2,8);
		cout<<num1<<" "<<str<<endl;
		if(index2>index1)
		{
			out<<str<<","<<num1<<endl;
		}
	}
	
	return 0;
}
