#include <stdio.h>
#include <stdlib.h>
#include <string.h>

char *getTxtFile(char *fn) 
{
	FILE *fp;
	char *data = NULL;

	int size=0;

	if (fn != NULL) 
	{
		fp = fopen(fn,"rt");

		if (fp != NULL) 
		{
			fseek(fp, 0, SEEK_END);
			size = ftell(fp);
			rewind(fp);
			if (size > 0) 
			{
				data = (char *)malloc(sizeof(char) * (size+1));
				size = fread(data,sizeof(char),size,fp);
				data[size] = '\0';
			}
			fclose(fp);
		}
	}
	return data;
}

int writeTxtFile(char *fn, char *s) 
{
	FILE *fp;
	int res = 0;
	if (fn != NULL) 
	{
		fp = fopen(fn,"w");
		if (fp != NULL) 
		{
			if (fwrite(s,sizeof(char),strlen(s),fp) == strlen(s))
				res = 1;
			fclose(fp);
		}
	}
	return(res);
}







