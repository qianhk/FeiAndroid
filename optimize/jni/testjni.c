
#include <stdio.h>
#include <string.h>

int mainxx(int argc,char *argv[])
{
	__asm__ ("mov r0,r0\n"
	"mov r0, r0\n"
	"mov r0, r0\n"
	"mov r0, r0\n"
	"mov r0, r0\n"
	"mov r0, r0\n"
	"mov r0, r0\n"
	"add r0, #125\n"
	"mov r0, r0\n"
	"mov r0, r0\n"
	"mov r0, r0\n"
	"mov r0, r0\n"
	"mov r0, r0\n"
	"mov r0, r0\n"
	"mov r0, r0\n"
	"mov r0, r0\n"
	);
	char name[]="helloworld";
	int  keys[]={0xb,0x1f,0x19,0x19,0x49,0xb,0xb,0xb,0x31,0x53};
	char Thekeys[11];
	int i;
	for(i=0;i<10;i++)
	{
		keys[i]^=7;
		keys[i]=keys[i]/6;
		keys[i]+=22;
		keys[i]-=24;
		keys[i]^=name[i];
	}
	for(i=0;i<10;i++)
	{
		Thekeys[i]=keys[i];
	}
	Thekeys[i]=0;
	if(argc > 1 && !strcmp(Thekeys,argv[1]))
		printf("Good Work,you have Successed!\n");
	else
		printf("NO,you are failed!\n");
	return 0;
}

uint64_t computeIterativelyFaster(uint n) {
	__asm__ ("mov r0,r0\n"
	"mov r0, r0\n"
	"mov r0, r0\n"
	"mov r0, r0\n"
	"mov r0, r0\n"
	"mov r0, r0\n"
	"mov r0, r0\n"
	"add r0, #124\n"
	"mov r0, r0\n"
	"mov r0, r0\n"
	"mov r0, r0\n"
	"mov r0, r0\n"
	"mov r0, r0\n"
	"mov r0, r0\n"
	"mov r0, r0\n"
	"mov r0, r0\n"
	);
	if (n > 1) {
		uint64_t a, b = 1;
		n--;
		a = n & 1;
		n /= 2;
		while (n-- > 0) {
			a += b;
			b += a;
		}
		return b;
	}
	return n;
}

int main(int argc,char *argv[]) {

	mainxx(argc, argv);

	uint64_t u64 = computeIterativelyFaster(33);
	printf("iterative=%lld\n", u64);

}