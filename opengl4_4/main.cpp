#include <stdio.h>
#include <stdlib.h>
#include "glew\include\GL\glew.h"
#include "glut\glut.h"
#include <memory.h>
#include <math.h>
#include "mymath.h"
#include <stack>

using namespace std;
 
#define PI       3.14159265358979323846

void drawTriangles();
void initPickingTexture();
void setTransMatrix(float *mat, float x, float y, float z);
void multiplyMatrix(float *a, float *b);
void xProduct( float *a, float *b, float *res);
void normalize(float *a);
void setIdentMatrix( float *mat, int size);
void placeCam(float posX, float posY, float posZ, float lookX, float lookY, float lookZ);
void setTransMatrix(float *mat, float x, float y, float z);
float * rotationMatrix(float x, float y, float z, float angle);
GLuint loadBMP_custom(char filename[]);
void enableWriting();
void disableWriting();
void readPixel(GLint x, GLint y);
void checkFrameBuffer();
void readIfObjectClicked();
struct vec3
{
	float x;
	float y;
	float z;
};

struct PixelInfo {
        unsigned int ObjectID;
        unsigned int DrawID;
        unsigned int PrimID;

        PixelInfo()     {
            ObjectID = 0;
            DrawID = 0;
            PrimID = 0;
        }
};
struct{GLubyte redComponent, greenComponent, blueComponent;} pixelColorInfo;
//lighting Coordinates
float lightX = 0.0f;
float lightY = 1.0f;
float lightZ = 1.5f;

//scaling coordinates
float scaleX = 1.0f;
float scaleY = 1.0f;
float scaleZ = 1.0f;
float screenHeight = 480.0f;
float screenWidth = 640.0f;
int lastMouseX;
int lastMouseY;

//light moving mode
int lightMode = 1;
GLuint lightModeLocation;

//placeCam(0,0,-10,0,0,-5);

//camera coordinates
float cameraX = 0.0f;
float cameraY = 0.0f;
float cameraZ = 10.0f;

//camera look at coordinates
float lookAtX = 0.0f;
float lookAtY = 0.0f;
float lookAtZ = 0.0f;

static PixelInfo pixelData;

//transparency values
float triangle1Alpha = 0.5f;
float triangle2Alpha = 0.5f;
bool transparent = false; 
bool animated = false;

float angle = 0.0f;
int mouseMode = 0;
GLint startX, startY;
bool mouseIsDown = false;
bool showTrueColor = false;


GLuint texturePointer, uvLoc, textureLocation;
GLuint frameBuffer, pickingTexture, depthTexture;
GLuint showTrueColorLocation;
GLuint currentViewMatrixLoc;

GLuint drawIndexLocation,drawIndex;
GLuint objectIndex, objectIndexLocation;

// vertices for triangle 1
float vertices1[] = {   -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            0.0f, 1.0f, 0.0f};
float normals1[] = {   -4.0f/3.0f, -4.0f/3.0f, -4.0f/3.0f,
            -4.0f/3.0f, -8.0f/3.0f, 4.0f/3.0f,
            0.0f, 0.0f, 4.0f,};
float colors1[] = { 1.0f, 0.0f, 0.0f, triangle1Alpha,
            1.0f, 0.0f, 0.0f, triangle1Alpha,
            1.0f, 0.0f, 0.0f, triangle1Alpha};
float uvVertices1[] = {
			0.0f, 0.0f,
			1.0f, 0.0f,
			0.5f, 1.0f};

//vertices for other triangle
float vertices2[] = {   -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            0.0f, 1.0f, 0.0f};
float normals2[] = {   -4.0f/3.0f, 0.0f, 4.0f/3.0f,
            -4.0f/3.0f, -4.0f/3.0f, 4.0f/3.0f,
            0.0f, 0.0f, 4.0f,};
float colors2[] = { 0.0f, 1.0f, 0.0f, triangle2Alpha,
            0.0f, 1.0f, 0.0f, triangle2Alpha,
            0.0f, 1.0f, 0.0f, triangle2Alpha};
float uvVertices2[] = {
			0.0f, 0.0f,
			1.0f, 0.0f,
			0.5f, 1.0f};

//vertices for 3rd triangle 
float vertices3[] = {   -1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, -1.0f,
            0.0f, 1.0f, 0.0f};
float normals3[] = {   -4.0f/3.0f, 0.0f, 4.0f/3.0f,
            -4.0f/3.0f, -4.0f/3.0f, 4.0f/3.0f,
            0.0f, 0.0f, 4.0f,};
float colors3[] = { 0.0f, 0.0f, 1.0f, triangle2Alpha,
            0.0f, 0.0f, 1.0f, triangle2Alpha,
            0.0f, 0.0f, 1.0f, triangle2Alpha};
float uvVertices3[] = {
			0.0f, 0.0f,
			1.0f, 0.0f,
			0.5f, 1.0f};

//vertices for 4th triangle 
float vertices4[] = {   1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, -1.0f,
            0.0f, 1.0f, 0.0f};
float normals4[] = {   -4.0f/3.0f, -4.0f/3.0f, 4.0f/3.0f,
            -4.0f/3.0f, -8.0f/3.0f, 4.0f/3.0f,
            0.0f, 0.0f, 4.0f,};
float colors4[] = { 1.0f, 0.0f, 1.0f, triangle2Alpha,
            1.0f, 0.0f, 1.0f, triangle2Alpha,
            1.0f, 0.0f, 1.0f, triangle2Alpha};
float uvVertices4[] = {
			0.0f, 0.0f,
			1.0f, 0.0f,
			0.5f, 1.0f};

//vertices for 5th triangle, one of the triangles for the bottom square 
float vertices5[] = {   -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f};
float normals5[] = {   -4.0f/3.0f, 0.0f, 4.0f/3.0f,
            -4.0f/3.0f, -8.0f/3.0f, 4.0f/3.0f,
            -4.0f/3.0f, -4.0f/3.0f, -4.0f/3.0f};
float colors5[] = { 1.0f, 1.0f, 0.0f, triangle2Alpha,
            1.0f, 1.0f, 0.0f, triangle2Alpha,
            1.0f, 1.0f, 0.0f, triangle2Alpha};
float uvVertices5[] = {
			0.0f, 1.0f,
			1.0f, 0.0f,
			0.0f, 0.0f}; 

//vertices for 6th triangle, one of the triangles for the bottom square 
float vertices6[] = {   -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, 1.0f};
float normals6[] = {   -4.0f/3.0f, 0.0f, 4.0f/3.0f,
            -4.0f/3.0f, -8.0f/3.0f, 4.0f/3.0f,
            -4.0f/3.0f, -4.0f/3.0f, 4.0f/3.0f};
float colors6[] = { 1.0f, 1.0f, 0.0f, triangle2Alpha,
            1.0f, 1.0f, 0.0f, triangle2Alpha,
            1.0f, 1.0f, 0.0f, triangle2Alpha};
float uvVertices6[] = {
			0.0f, 1.0f,
			1.0f, 0.0f,
			1.0f, 1.0f};

// shader names
char *vertexFileName = "vertex.txt";
char *fragmentFileName = "frag.txt";
 
// program and shader Id
GLuint p,v,f;
 
// vert attrib locations
GLuint vertexLoc, colorLoc,normalLoc;
GLuint angleLoc;
GLuint axisLocation, fovLocation, ratioLocation, nearPlaneLocation, farPlaneLocation;
GLuint lightLocation;

//translation information
GLfloat xdistance, ydistance, zdistance;

// uniform var locations
GLuint projMatrixLoc, viewMatrixLoc;
GLuint mouseCoordLocation;

//whether or not the lights are on
bool specular = true;
bool ambient = true;
bool diffuse = true;
GLuint ambientLocation, specularLocation, diffuseLocation;

//matrix stack
static bool currentMatrix_modelView = true;
static bool currentMatrix_projection = false;
static stack<float*> modelViewMatrix_stack;
static stack<float*> projectionMatrix_stack;
 
// vert array obj Id
GLuint vert[10];
 
// storage for matrices
float* projMatrix = new float[16];
float* viewMatrix = new float[16];
float* currentViewMatrix = new float[16];

float myAngle = 0.0f;
float myAngle2 = 0.0f;

int frame=0,time,timebase=0;
char s[50];

// vector opt
// res = a cross b;
void xProduct( float *a, float *b, float *res) 
{
 
    res[0] = a[1] * b[2]  -  b[1] * a[2];
    res[1] = a[2] * b[0]  -  b[2] * a[0];
    res[2] = a[0] * b[1]  -  b[0] * a[1];
}

void zrd_glInit(){

	float* init = new float[16];
	setIdentMatrix(init,4);

	if(currentMatrix_projection){
		projectionMatrix_stack.push(init);
	}else{
		modelViewMatrix_stack.push(init);
	}

}

void zrd_glPushMatrix(){
	float* initMatrix = new float[16];

	if(currentMatrix_projection){
		initMatrix = projectionMatrix_stack.top();
		projectionMatrix_stack.push(initMatrix);
	}else{
		initMatrix = modelViewMatrix_stack.top();
		modelViewMatrix_stack.push(initMatrix);
	}
}

void zrd_glPopMatrix(){
	if(currentMatrix_projection){
		projectionMatrix_stack.pop();
	}else{
		modelViewMatrix_stack.pop();
	}
}

void zrd_glRotatef(float angle, float x, float y, float z){
	float* rotMatrix = new float[16];
	float* resultMatrix = new float[16];

	if(currentMatrix_projection){
		if(projectionMatrix_stack.size() == 0){
			zrd_glInit();
		}
		resultMatrix = projectionMatrix_stack.top();
	}else{
		if(modelViewMatrix_stack.size() == 0){
			zrd_glInit();
		}
		resultMatrix = modelViewMatrix_stack.top();
	}

	rotMatrix = rotationMatrix(x,y,z,angle);
	multiplyMatrix(resultMatrix,rotMatrix);	

	if(currentMatrix_projection){
		projectionMatrix_stack.pop();
		projectionMatrix_stack.push(resultMatrix);
	}else{
		modelViewMatrix_stack.pop();
		modelViewMatrix_stack.push(resultMatrix);
	}



}

void zrd_glTranslatef(float x, float y, float z){
	float* translationMatrix = new float[16];
	float* resultMatrix = new float[16];

	if(currentMatrix_projection){
		if(projectionMatrix_stack.size() == 0){
			zrd_glInit();
		}
		resultMatrix = projectionMatrix_stack.top();
	}else{
		if(modelViewMatrix_stack.size() == 0){
			zrd_glInit();
		}
		resultMatrix = modelViewMatrix_stack.top();
	}

	setTransMatrix(translationMatrix,x,y,z);
	multiplyMatrix(resultMatrix,translationMatrix);	

	if(currentMatrix_projection){
		projectionMatrix_stack.pop();
		projectionMatrix_stack.push(resultMatrix);
	}else{
		modelViewMatrix_stack.pop();
		modelViewMatrix_stack.push(resultMatrix);
	}



}

void zrd_glScalef(float x, float y, float z){
	float* resultMatrix = new float[16];
	float* scaleMatrix = new float[16];

	for(int index = 0; index < 16; index++){
		scaleMatrix[index] = 0.0f;
	}
	scaleMatrix[0] = x;
	scaleMatrix[5] = y;
	scaleMatrix[10] = z;
	scaleMatrix[15] = 1;

	if(modelViewMatrix_stack.size() == 0){
		zrd_glInit();
	}
	resultMatrix = modelViewMatrix_stack.top();

	multiplyMatrix(resultMatrix,scaleMatrix);

	modelViewMatrix_stack.pop();
	modelViewMatrix_stack.push(resultMatrix);
}
 
// normalize a vec3
void normalize(float *a) 
{
 
    float mag = sqrt(a[0] * a[0]  +  a[1] * a[1]  +  a[2] * a[2]);
    a[0] /= mag;
    a[1] /= mag;
    a[2] /= mag;
}

// Matrix Opt. - In Opengl 3 we need to handle our own matrix.
 
// In this form : a = a * b; 
void multiplyMatrix(float *a, float *b) 
{
    float res[16];
 
    for (int i = 0; i < 4; ++i) {
        for (int j = 0; j < 4; ++j) {
            res[j*4 + i] = 0.0f;
            for (int k = 0; k < 4; ++k) {
                res[j*4 + i] += a[k*4 + i] * b[j*4 + k];
            }
        }
    }
    memcpy(a, res, 16 * sizeof(float));
}

void multiplyMatrixAndVector(float *mat, float *vec, float* res) 
{
    res = new float[4];
 
    for (int i = 0; i < 4; ++i) {
		res[i] = 0.0f;
        for (int j = 0; j < 4; ++j) {
            res[i] = res[i] + mat[4*i + j]*vec[i];
        }
    }
}

// sets the square matrix mat to the ID matrix,
void setIdentMatrix( float *mat, int size) 
{
 
    // 0s
    for (int i = 0; i < size * size; ++i)
            mat[i] = 0.0f;
 
    // diagonal 1s
    for (int i = 0; i < size; ++i)
        mat[i + i * size] = 1.0f;
}
 
// View Matrix
// just like glulookat
void placeCam(float posX, float posY, float posZ, float lookX, float lookY, float lookZ) 
{
 
    float dir[3], right[3], up[3];
 
    up[0] = 0.0f;   up[1] = 1.0f;   up[2] = 0.0f;
 
    dir[0] =  (lookX - posX);
    dir[1] =  (lookY - posY);
    dir[2] =  (lookZ - posZ);
    normalize(dir);
 
	// this order matters
    xProduct(dir,up,right);
    normalize(right);
 
    xProduct(right,dir,up);
    normalize(up);
 
    float aux[16];
	
	float* tempViewMatrix = new float[16];
    tempViewMatrix[0]  = right[0];
	tempViewMatrix[1]  = up[0];
	tempViewMatrix[2]  = -dir[0];
	tempViewMatrix[3]  = 0.0f;
    tempViewMatrix[4]  = right[1];
	tempViewMatrix[5]  = up[1];
	tempViewMatrix[6]  = -dir[1];
	tempViewMatrix[7]  = 0.0f;
    tempViewMatrix[8]  = right[2];
	tempViewMatrix[9]  = up[2];
	tempViewMatrix[10] = -dir[2];
	tempViewMatrix[11] = 0.0f;
    tempViewMatrix[12] = 0.0f;
    tempViewMatrix[13] = 0.0f;
    tempViewMatrix[14] =  0.0f;
    tempViewMatrix[15] = 1.0f;




    setTransMatrix(aux, -posX, -posY, -posZ);
    multiplyMatrix(tempViewMatrix, aux);

	currentViewMatrix = new float[16];
	for(int index = 0; index < 16; index++){
		currentViewMatrix[index] = tempViewMatrix[index];
	}

	if(modelViewMatrix_stack.size() == 0){
		zrd_glInit();
	}

	modelViewMatrix_stack.pop();
	modelViewMatrix_stack.push(tempViewMatrix);

	//currentViewMatrix = tempViewMatrix;
	
	// you should do this instead. If you want to apply rotation to your viewMatrix.
	//multiplyMatrix(viewMatrix, rotationMatrix(0.0,0.0,1.0, 0.785)); 
}

// Generates a rotation matrix.  Angle is in radian.
float * rotationMatrix(float x, float y, float z, float angle)
{
	float tempVec[3];
	tempVec[0] = x;
	tempVec[1] = y;
	tempVec[2] = z;
    normalize(tempVec);

	vec3 axis;
	axis.x = tempVec[0];
	axis.y = tempVec[1];
	axis.z = tempVec[2];

    float s = sin(angle);
    float c = cos(angle);
    float oc = 1.0 - c;

	float mat[16] = {oc * axis.x * axis.x + c,           oc * axis.x * axis.y - axis.z * s,  oc * axis.z * axis.x + axis.y * s,  0.0,
                oc * axis.x * axis.y + axis.z * s,  oc * axis.y * axis.y + c,           oc * axis.y * axis.z - axis.x * s,  0.0,
                oc * axis.z * axis.x - axis.y * s,  oc * axis.y * axis.z + axis.x * s,  oc * axis.z * axis.z + c,           0.0,
				0.0,                                0.0,                                0.0,                                1.0};

    return mat;

}
 
// Projection Matrix
//gluPerspective
void buildProjMatrix(float fov, float ratio, float nearP, float farP) {
 
    float f = 1.0f / tan (fov * (PI / 360.0));
	float* projectionMatrix = new float[16];
    setIdentMatrix(projectionMatrix,4);
    projectionMatrix[0] = f / ratio;
    projectionMatrix[1 * 4 + 1] = f;
    projectionMatrix[2 * 4 + 2] = (farP + nearP) / (nearP - farP);
    projectionMatrix[3 * 4 + 2] = (2.0f * farP * nearP) / (nearP - farP);
    projectionMatrix[2 * 4 + 3] = -1.0f;
    projectionMatrix[3 * 4 + 3] = 0.0f;

	if(projectionMatrix_stack.size() == 0){
		zrd_glInit();
	}
	projectionMatrix_stack.pop();
	projectionMatrix_stack.push(projectionMatrix);
}

// Transformation matrix mat with a translation
void setTransMatrix(float *mat, float x, float y, float z) {
 
    setIdentMatrix(mat,4);
    mat[12] = x;
    mat[13] = y;
    mat[14] = z;
}

void changeSize(int w, int h) {
 
    float ratio;

    // place viewport to be the entire window
    glViewport(0, 0, w, h);
    ratio = (1.0f * w) / h;

	screenHeight = h;
	screenWidth = w;

    buildProjMatrix(53.13f, ratio, 1.0f, 30.0f);
}
 
void setupBuffers() {
 
    GLuint buffers[4];
	GLuint nextBuffers[4];
	GLuint thirdBuffers[4];
	GLuint fourthBuffers[4];
	GLuint fifthBuffers[4];
	GLuint sixthBuffers[4];
 
    glGenVertexArrays(6, vert);

	//-----------------------------------------------------
    // first triangle
    glBindVertexArray(vert[0]);
    // generate 2 buffers for vert and color
    glGenBuffers(4, buffers);

    // bind buffer for vertices and copy data into buffer
    glBindBuffer(GL_ARRAY_BUFFER, buffers[0]);
    glBufferData(GL_ARRAY_BUFFER, sizeof(vertices1), vertices1, GL_STATIC_DRAW);
    glEnableVertexAttribArray(vertexLoc);
    glVertexAttribPointer(vertexLoc, 3, GL_FLOAT, 0, 0, 0);
 
    // bind buffer for colors and copy data into buffer
    glBindBuffer(GL_ARRAY_BUFFER, buffers[1]);
    glBufferData(GL_ARRAY_BUFFER, sizeof(colors1), colors1, GL_STATIC_DRAW);
    glEnableVertexAttribArray(colorLoc);
    glVertexAttribPointer(colorLoc, 4, GL_FLOAT, 0, 0, 0);

	//bind buffer for normals and copy data into buffer
	glBindBuffer(GL_ARRAY_BUFFER,buffers[2]);
	glBufferData(GL_ARRAY_BUFFER, sizeof(normals1), normals1, GL_STATIC_DRAW);
	glEnableVertexAttribArray(normalLoc);
    glVertexAttribPointer(normalLoc, 3, GL_FLOAT, 0, 0, 0);

	//bind buffer for textures
    glBindBuffer(GL_ARRAY_BUFFER, buffers[3]);
    glBufferData(GL_ARRAY_BUFFER, sizeof(uvVertices1), uvVertices1, GL_STATIC_DRAW);
	glEnableVertexAttribArray(uvLoc);
	glVertexAttribPointer(uvLoc, 2, GL_FLOAT, 0, 0, 0);

	//--------------------------------------------------------
	// second triangle
    glBindVertexArray(vert[1]);
    // generate 2 buffers for vert and color
	glGenBuffers(4, nextBuffers);
    // bind buffer for vertices and copy data into buffer
	glBindBuffer(GL_ARRAY_BUFFER, nextBuffers[0]);
    glBufferData(GL_ARRAY_BUFFER, sizeof(vertices2), vertices2, GL_STATIC_DRAW);
    glEnableVertexAttribArray(vertexLoc);
    glVertexAttribPointer(vertexLoc, 3, GL_FLOAT, 0, 0, 0);
 
    // bind buffer for colors and copy data into buffer
    glBindBuffer(GL_ARRAY_BUFFER, nextBuffers[1]);
    glBufferData(GL_ARRAY_BUFFER, sizeof(colors2), colors2, GL_STATIC_DRAW);
    glEnableVertexAttribArray(colorLoc);
    glVertexAttribPointer(colorLoc, 4, GL_FLOAT, 0, 0, 0);

	//bind buffer for normals and copy data into buffer
	glBindBuffer(GL_ARRAY_BUFFER,nextBuffers[2]);
	glBufferData(GL_ARRAY_BUFFER, sizeof(normals2), normals2, GL_STATIC_DRAW);
	glEnableVertexAttribArray(normalLoc);
    glVertexAttribPointer(normalLoc, 3, GL_FLOAT, 0, 0, 0);

	//bind buffer for textures
    glBindBuffer(GL_ARRAY_BUFFER, nextBuffers[3]);
    glBufferData(GL_ARRAY_BUFFER, sizeof(uvVertices2), uvVertices2, GL_STATIC_DRAW);
	glEnableVertexAttribArray(uvLoc);
	glVertexAttribPointer(uvLoc, 2, GL_FLOAT, 0, 0, 0);

	//--------------------------------------------------------
	// third triangle
    glBindVertexArray(vert[2]);
    // generate 2 buffers for vert and color
	glGenBuffers(4, thirdBuffers);
    // bind buffer for vertices and copy data into buffer
	glBindBuffer(GL_ARRAY_BUFFER, thirdBuffers[0]);
    glBufferData(GL_ARRAY_BUFFER, sizeof(vertices3), vertices3, GL_STATIC_DRAW);
    glEnableVertexAttribArray(vertexLoc);
    glVertexAttribPointer(vertexLoc, 3, GL_FLOAT, 0, 0, 0);
 
    // bind buffer for colors and copy data into buffer
    glBindBuffer(GL_ARRAY_BUFFER, thirdBuffers[1]);
    glBufferData(GL_ARRAY_BUFFER, sizeof(colors3), colors3, GL_STATIC_DRAW);
    glEnableVertexAttribArray(colorLoc);
    glVertexAttribPointer(colorLoc, 4, GL_FLOAT, 0, 0, 0);

	//bind buffer for normals and copy data into buffer
	glBindBuffer(GL_ARRAY_BUFFER,thirdBuffers[2]);
	glBufferData(GL_ARRAY_BUFFER, sizeof(normals3), normals3, GL_STATIC_DRAW);
	glEnableVertexAttribArray(normalLoc);
    glVertexAttribPointer(normalLoc, 3, GL_FLOAT, 0, 0, 0);

	//bind buffer for textures
    glBindBuffer(GL_ARRAY_BUFFER, thirdBuffers[3]);
    glBufferData(GL_ARRAY_BUFFER, sizeof(uvVertices3), uvVertices3, GL_STATIC_DRAW);
	glEnableVertexAttribArray(uvLoc);
	glVertexAttribPointer(uvLoc, 2, GL_FLOAT, 0, 0, 0);

	//--------------------------------------------------------
	// fourth triangle
    glBindVertexArray(vert[3]);
    // generate 2 buffers for vert and color
	glGenBuffers(4, fourthBuffers);
    // bind buffer for vertices and copy data into buffer
	glBindBuffer(GL_ARRAY_BUFFER, fourthBuffers[0]);
    glBufferData(GL_ARRAY_BUFFER, sizeof(vertices4), vertices4, GL_STATIC_DRAW);
    glEnableVertexAttribArray(vertexLoc);
    glVertexAttribPointer(vertexLoc, 3, GL_FLOAT, 0, 0, 0);
 
    // bind buffer for colors and copy data into buffer
    glBindBuffer(GL_ARRAY_BUFFER, fourthBuffers[1]);
    glBufferData(GL_ARRAY_BUFFER, sizeof(colors4), colors4, GL_STATIC_DRAW);
    glEnableVertexAttribArray(colorLoc);
    glVertexAttribPointer(colorLoc, 4, GL_FLOAT, 0, 0, 0);

	//bind buffer for normals and copy data into buffer
	glBindBuffer(GL_ARRAY_BUFFER,fourthBuffers[2]);
	glBufferData(GL_ARRAY_BUFFER, sizeof(normals4), normals4, GL_STATIC_DRAW);
	glEnableVertexAttribArray(normalLoc);
    glVertexAttribPointer(normalLoc, 3, GL_FLOAT, 0, 0, 0);

	//bind buffer for textures
    glBindBuffer(GL_ARRAY_BUFFER, fourthBuffers[3]);
    glBufferData(GL_ARRAY_BUFFER, sizeof(uvVertices4), uvVertices4, GL_STATIC_DRAW);
	glEnableVertexAttribArray(uvLoc);
	glVertexAttribPointer(uvLoc, 2, GL_FLOAT, 0, 0, 0);

	//--------------------------------------------------------
	// fifth triangle
    glBindVertexArray(vert[4]);
    // generate 2 buffers for vert and color
	glGenBuffers(4, fifthBuffers);
    // bind buffer for vertices and copy data into buffer
	glBindBuffer(GL_ARRAY_BUFFER, fifthBuffers[0]);
    glBufferData(GL_ARRAY_BUFFER, sizeof(vertices5), vertices5, GL_STATIC_DRAW);
    glEnableVertexAttribArray(vertexLoc);
    glVertexAttribPointer(vertexLoc, 3, GL_FLOAT, 0, 0, 0);
 
    // bind buffer for colors and copy data into buffer
    glBindBuffer(GL_ARRAY_BUFFER, fifthBuffers[1]);
    glBufferData(GL_ARRAY_BUFFER, sizeof(colors5), colors5, GL_STATIC_DRAW);
    glEnableVertexAttribArray(colorLoc);
    glVertexAttribPointer(colorLoc, 4, GL_FLOAT, 0, 0, 0);

	//bind buffer for normals and copy data into buffer
	glBindBuffer(GL_ARRAY_BUFFER,fifthBuffers[2]);
	glBufferData(GL_ARRAY_BUFFER, sizeof(normals5), normals5, GL_STATIC_DRAW);
	glEnableVertexAttribArray(normalLoc);
    glVertexAttribPointer(normalLoc, 3, GL_FLOAT, 0, 0, 0);

	//bind buffer for textures
    glBindBuffer(GL_ARRAY_BUFFER, fifthBuffers[3]);
    glBufferData(GL_ARRAY_BUFFER, sizeof(uvVertices5), uvVertices5, GL_STATIC_DRAW);
	glEnableVertexAttribArray(uvLoc);
	glVertexAttribPointer(uvLoc, 2, GL_FLOAT, 0, 0, 0);
	//--------------------------------------------------------
	// sixth triangle
    glBindVertexArray(vert[5]);
    // generate 2 buffers for vert and color
	glGenBuffers(4, sixthBuffers);
    // bind buffer for vertices and copy data into buffer
	glBindBuffer(GL_ARRAY_BUFFER, sixthBuffers[0]);
    glBufferData(GL_ARRAY_BUFFER, sizeof(vertices6), vertices6, GL_STATIC_DRAW);
    glEnableVertexAttribArray(vertexLoc);
    glVertexAttribPointer(vertexLoc, 3, GL_FLOAT, 0, 0, 0);
 
    // bind buffer for colors and copy data into buffer
    glBindBuffer(GL_ARRAY_BUFFER, sixthBuffers[1]);
    glBufferData(GL_ARRAY_BUFFER, sizeof(colors6), colors6, GL_STATIC_DRAW);
    glEnableVertexAttribArray(colorLoc);
    glVertexAttribPointer(colorLoc, 4, GL_FLOAT, 0, 0, 0);

	//bind buffer for normals and copy data into buffer
	glBindBuffer(GL_ARRAY_BUFFER,sixthBuffers[2]);
	glBufferData(GL_ARRAY_BUFFER, sizeof(normals6), normals6, GL_STATIC_DRAW);
	glEnableVertexAttribArray(normalLoc);
    glVertexAttribPointer(normalLoc, 3, GL_FLOAT, 0, 0, 0);

	//bind buffer for textures
    glBindBuffer(GL_ARRAY_BUFFER, sixthBuffers[3]);
    glBufferData(GL_ARRAY_BUFFER, sizeof(uvVertices6), uvVertices6, GL_STATIC_DRAW);
	glEnableVertexAttribArray(uvLoc);
	glVertexAttribPointer(uvLoc, 2, GL_FLOAT, 0, 0, 0);
}
 
void setUniforms() {
 
    // must be called after glUseProgram
	// set the variables for the shader
	projMatrix = projectionMatrix_stack.top();
	viewMatrix = modelViewMatrix_stack.top();
    glUniformMatrix4fv(projMatrixLoc,  1, false, projMatrix);
    glUniformMatrix4fv(viewMatrixLoc,  1, false, viewMatrix);
	glUniformMatrix4fv(currentViewMatrixLoc,1,false,currentViewMatrix);

	float vector[] = {0,1,0,0};
	glUniform4fv(axisLocation,1,vector);
	glUniform1f(angleLoc,myAngle);

	float theRatio = screenWidth/screenHeight;
	buildProjMatrix(53.13f,theRatio,1.0f,30.0f);

	glUniform1f(fovLocation,53.13);
	glUniform1f(ratioLocation,theRatio);
	glUniform1f(nearPlaneLocation,1);
	glUniform1f(farPlaneLocation,30);

	glUniform3f(lightLocation,lightX,lightY,lightZ);
	glUniform2f(mouseCoordLocation,startX,startY);
	glUniform1i(textureLocation,0);

	glUniform1i(ambientLocation,ambient);
	glUniform1i(diffuseLocation,diffuse);
	glUniform1i(specularLocation,specular);
	glUniform1i(showTrueColorLocation,showTrueColor);
	glUniform1ui(drawIndexLocation, drawIndex);
	glUniform1ui(objectIndexLocation, objectIndex);

	glUniform1i(lightModeLocation,lightMode);
}
 
void renderScene(void) {

	frame++;
	time=glutGet(GLUT_ELAPSED_TIME);
	if (time - timebase > 1000) {
		sprintf(s,"FPS:%4.2f",
			frame*1000.0/(time-timebase));
		timebase = time;
		frame = 0;
	}
    glutSetWindowTitle(s);
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	if(transparent){
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA,GL_SRC_ALPHA);
	}else{
		glDisable(GL_BLEND);
	}

    //placeCam(10,2,10,0,2,-5);
	//placeCam(0,0,-10,0,0,-5);
	placeCam(cameraX,cameraY,cameraZ,lookAtX,lookAtY,lookAtZ);
	zrd_glTranslatef(xdistance,ydistance,-zdistance);
	zrd_glScalef(scaleX,scaleY,scaleZ);
	zrd_glRotatef(myAngle2,1.0,0.0,0.0);
	zrd_glRotatef(myAngle,0.0,1.0,0.0);
	
    glUseProgram(p);
    setUniforms();
	
	if(mouseIsDown){
		
		/*

		This is code to try and do the picking using the frame buffer
		It did not end up working due to the following error being returned by 
			the check of the frame buffer status:
			GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT​

		enableWriting();
		drawTriangles();
		disableWriting();
		
		readPixel(startX,startY);
		printf("%d ",pixelData.DrawID);
		printf("%d ",pixelData.ObjectID);
		printf("%d\n",pixelData.PrimID);*/

		drawTriangles();
		readIfObjectClicked();

		
	}else{
		showTrueColor = false;
	}

	
	drawTriangles();
	//zrd_glTranslatef(0,0,4);
	
	//drawTriangles();
	checkFrameBuffer();
   glutSwapBuffers();
}

void drawTriangles(){

	objectIndex = 1;

	drawIndex = 1;
	glBindVertexArray(vert[0]);
    glDrawArrays(GL_TRIANGLES, 0, 3);

	drawIndex = 2;
	glBindVertexArray(vert[1]);
	glDrawArrays(GL_TRIANGLES, 0, 3);

	drawIndex = 3;
	glBindVertexArray(vert[2]);
	glDrawArrays(GL_TRIANGLES, 0, 3);

	drawIndex = 4;
	glBindVertexArray(vert[3]);
	glDrawArrays(GL_TRIANGLES, 0, 3);

	drawIndex = 5;
	glBindVertexArray(vert[4]);
	glDrawArrays(GL_TRIANGLES, 0, 3);

	drawIndex = 6;
	glBindVertexArray(vert[5]);
	glDrawArrays(GL_TRIANGLES, 0, 3);
}
 

void printShaderInfoLog(GLuint obj)
{
    int infologLength = 0;
    int charsWritten  = 0;
    char *infoLog;
 
    glGetShaderiv(obj, GL_INFO_LOG_LENGTH,&infologLength);
 
    if (infologLength > 0)
    {
        infoLog = (char *)malloc(infologLength);
        glGetShaderInfoLog(obj, infologLength, &charsWritten, infoLog);
        printf("%s\n",infoLog);
        free(infoLog);
    }
}
 
void printProgramInfoLog(GLuint obj)
{
    int infologLength = 0;
    int charsWritten  = 0;
    char *infoLog;
 
    glGetProgramiv(obj, GL_INFO_LOG_LENGTH,&infologLength);
 
    if (infologLength > 0)
    {
        infoLog = (char *)malloc(infologLength);
        glGetProgramInfoLog(obj, infologLength, &charsWritten, infoLog);
        printf("%s\n",infoLog);
        free(infoLog);
    }
}
 
GLuint initShaders() {
 
    char *vertShader = NULL,*fragShader = NULL;
 
    GLuint p,v,f;
 
    v = glCreateShader(GL_VERTEX_SHADER);
    f = glCreateShader(GL_FRAGMENT_SHADER);
    vertShader = getTxtFile(vertexFileName);
    fragShader = getTxtFile(fragmentFileName);
    const char * vv = vertShader;
    const char * ff = fragShader;
    glShaderSource(v, 1, &vv,NULL);
    glShaderSource(f, 1, &ff,NULL);
    free(vertShader);
	free(fragShader);
    glCompileShader(v);
    glCompileShader(f);
    printShaderInfoLog(v);
    printShaderInfoLog(f);
    p = glCreateProgram();
    glAttachShader(p,v);
    glAttachShader(p,f);
    glBindFragDataLocation(p, 0, "outputF");
    glLinkProgram(p);
    printProgramInfoLog(p);
    vertexLoc = glGetAttribLocation(p,"position");
    colorLoc = glGetAttribLocation(p, "color"); 
	normalLoc = glGetAttribLocation(p, "normal");
	uvLoc = glGetAttribLocation(p,"vertexUV");

    projMatrixLoc = glGetUniformLocation(p, "projMatrix");
    viewMatrixLoc = glGetUniformLocation(p, "viewMatrix");
	angleLoc = glGetUniformLocation(p,"ang_i");
	nearPlaneLocation = glGetUniformLocation(p,"nearP");
	farPlaneLocation = glGetUniformLocation(p,"farP");
	axisLocation = glGetUniformLocation(p,"axis_i");
	fovLocation = glGetUniformLocation(p,"fov");
	ratioLocation = glGetUniformLocation(p,"ratio");
	textureLocation = glGetUniformLocation(p,"zrdTextureSampler");
	lightLocation = glGetUniformLocation(p,"lightCoordinates");
	mouseCoordLocation = glGetUniformLocation(p,"mouseCoordinates");
	ambientLocation = glGetUniformLocation(p,"ambientOn");
	diffuseLocation = glGetUniformLocation(p,"diffuseOn");
	specularLocation = glGetUniformLocation(p,"specularOn");
	showTrueColorLocation = glGetUniformLocation(p,"showTrueColor");

	drawIndexLocation = glGetUniformLocation(p,"gDrawIndex");
	objectIndexLocation = glGetUniformLocation(p,"gObjectIndex");
	currentViewMatrixLoc = glGetUniformLocation(p,"currentViewMatrix");

	lightModeLocation = glGetUniformLocation(p,"lightMode");

    return(p);
}

float deltaAngle = 0.0f;
int xOrigin = -1;


void readKeyboard( unsigned char key, int x, int y ){
	float newRange;
	//printf("Key Pressed: %d\n",key);
  switch( key ){
	  case  0x1B: /* esc */
	  case 'b':
		  lightMode = (lightMode + 1)%3;
		  printf("Light Mode: %d\n",lightMode);
		  if(lightMode == 0){
			  printf("Light is stationary in scene\n\n");
		  }else if(lightMode == 1){
			  printf("Light stays stationary with respect to object and camera\n\n");
		  }else if(lightMode == 2){
			  printf("Light stays stationary with respect to camera but not object\n\n");
		  }
		  break;
	  case  't':
		  transparent = !transparent;
		break;

		//scaling
	  case 'q':
		  scaleX = scaleX + 0.05;
		  scaleY = scaleY + 0.05;
		  scaleZ = scaleZ + 0.05;
		  break;
	  case 'z':
		  scaleX = scaleX - 0.05;
		  scaleY = scaleY - 0.05;
		  scaleZ = scaleZ - 0.05;

		  break;

		  //light movements
	  case 'w':
		  lightY = lightY + 0.1;
		  break;
	  case 's':
		  lightY = lightY - 0.1;
		  break;
	  case 'a':
		  lightX = lightX - 0.1;
		  break;
	  case 'd':
		  lightX = lightX + 0.1;
		  break;
	  case 'r':
		  lightZ = lightZ - 0.1;
		  break;
	  case 'f':
		  lightZ = lightZ + 0.1;
		  break;
	  case 'g':
		  animated = !animated;
		  break;

		  //camera movements
	  case 'i':
		  cameraZ = cameraZ + 0.1;
		  lookAtZ = lookAtZ + 0.1;
		  break;
	  case 'k':
		  cameraZ = cameraZ - 0.1;
		  lookAtZ = lookAtZ - 0.1;
		  break;
	  case 'j':
		  cameraX = cameraX + 0.1;
		  lookAtX = lookAtX + 0.1;
		  break;
	  case 'l':
		  cameraX = cameraX - 0.1;
		  lookAtX = lookAtX - 0.1;
		  break;
	  case 'y':
		  cameraY = cameraY - 0.1;
		  lookAtY = lookAtY - 0.1;
		  break;
	  case 'h':
		  cameraY = cameraY + 0.1;
		  lookAtY = lookAtY + 0.1;
		  break;

		  //look at movements
	  case 'p':
		  lookAtY = lookAtY - 0.1;
		  break;
	  case 'm':
		  lookAtY = lookAtY + 0.1;
		  break;
	  case 'o':
		  lookAtX = lookAtX - 0.1;
		  break;
	  case 'n':
		  lookAtX = lookAtX + 0.1;
		  break;

		  //turn on/off different lighting modes
	  case 'x':
		  ambient = !ambient;
		  break;
	  case 'c':
		  specular = !specular;
		  break;
	  case 'v':
		  diffuse = !diffuse;
		  break;

		  //make translation possible with keyboard
	  case '2':
		  //down in number pad
		  ydistance = ydistance - 0.1;
		  break;

	  case '4':
		  //left in number pad
		  xdistance = xdistance - 0.1;
		  break;

	  case '6':
		  //right in number pad
		  xdistance = xdistance + 0.1;
		  break;

	  case '8':
		  //up in number pad
		  ydistance = ydistance + 0.1;
		  break;
	  glutPostRedisplay( );
	}
}


//This event will trigger when you have a mouse button pressed down.
void mouseMove(int x, int y) 
{
	// x and y is the mouse position.
	float factor;
	if(mouseMode == 1){

		//rotation mode
		myAngle = myAngle - (x - startX)/10.0;
		myAngle2 = myAngle2 - (y - startY)/10.0;
		startX = x;
		startY = y;

	}else if(mouseMode == 2){

		//translation mode

		xdistance = xdistance + (x - startX)/100.0;
		ydistance = ydistance - (y - startY)/100.0;
		startX = x;
		startY = y;
		

	}else if(mouseMode == 3){

		//zooming mode
		zdistance = zdistance + (y - startY)/10.0;
		startX = x;
		startY = y;
	}
}

void readIfObjectClicked(){
	glReadPixels(startX,screenHeight-startY,1,1,GL_RGB,GL_UNSIGNED_BYTE,&pixelColorInfo);
	//printf("\nColor=%d,",pixelColorInfo.redComponent);
	//printf("%d,",pixelColorInfo.greenComponent);
	//printf("%d\n",pixelColorInfo.blueComponent);
	if(pixelColorInfo.redComponent == 128 && pixelColorInfo.greenComponent == 128 && pixelColorInfo.blueComponent == 128){
		showTrueColor = false;
	}else{
		showTrueColor = true;
	}
}


//This event occur when you press a mouse button.
void mouseButton(int button, int state, int x, int y) 
{

	if(state == GLUT_UP){
		mouseIsDown = false;
		mouseMode = 0;
	}else{

		mouseIsDown = true;

		// only start motion if the left button is pressed
		if (button == GLUT_LEFT_BUTTON) 
		{
			mouseMode = 1;
			startX = x;
			startY = y;
		}else if(button == GLUT_MIDDLE_BUTTON){
			mouseMode = 2;
			startX = x;
			startY = y;
		}else if(button == GLUT_RIGHT_BUTTON){
			mouseMode = 3;
			startX = x;
			startY = y;
		}
	}

	
}

int iterationNumber = 0;
void rotateIdle(){
	if(animated){
		myAngle += 0.0005;
	}
	glutPostRedisplay();
}
 
int main(int argc, char **argv) 
{
	// sets up glut
    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_DEPTH | GLUT_DOUBLE | GLUT_RGBA);
    glutInitWindowPosition(100,100);
    glutInitWindowSize(640,480);
    glutCreateWindow("ICS Graphics");
    glutSetWindowTitle(s);
	// call back functions
    glutDisplayFunc(renderScene);
    glutIdleFunc(rotateIdle);
    glutReshapeFunc(changeSize);

	glutKeyboardFunc(readKeyboard);
	glutMouseFunc(mouseButton);
	glutMotionFunc(mouseMove);
 
	// check if a particular extension is available on your platform
    glewInit();
    if (glewIsSupported("GL_VERSION_3_3"))
        printf("OpenGL 3.3 is supported\n");
    else 
	{
        printf("OpenGL 3.3 not supported\n");
        exit(1);
    }

	initPickingTexture();
	char* filename = "chessBoard_wood_bmp.bmp";
	texturePointer = loadBMP_custom(filename);
    glEnable(GL_DEPTH_TEST);
    glClearColor(0.5,0.5,0.5,1.0);
    p = initShaders(); 
    setupBuffers(); 
	printf("Initial Light Mode: 1\n");
	printf("Light stays stationary with respect to object and camera\n\n");
    glutMainLoop();

    return(0); 
}

// From http://www.opengl-tutorial.org/beginners-tutorials/tutorial-5-a-textured-cube/ 
GLuint loadBMP_custom(char filename[]){

    // Data read from the header of the BMP file
    unsigned char header[54]; // Each BMP file begins by a 54-bytes header
    unsigned int dataPos;     // Position in the file where the actual data begins
    unsigned int width, height;
    unsigned int imageSize;   // = width*height*3
    // Actual RGB data
    unsigned char * data;


    FILE * file = fopen(filename,"rb");

    if ( fread(header, 1, 54, file)!=54 ){ // If not 54 bytes read : problem
        printf("Not a correct BMP file\n");
        return false;
    }

    if ( header[0]!='B' || header[1]!='M' ){
        printf("Not a correct BMP file\n");
        return 0;
    }

    printf("\nInside bmp_reader ");

    // Read ints from the byte array
    dataPos    = *(int*)&(header[0x0A]);
    imageSize  = *(int*)&(header[0x22]);
    width      = *(int*)&(header[0x12]);
    height     = *(int*)&(header[0x16]);

    // Some BMP files are misformatted, guess missing information
    if (imageSize==0)    imageSize=width*height*3; // 3 : one byte for each Red, Green and Blue component
    if (dataPos==0)      dataPos=54; // The BMP header is done that way

    // Create a buffer
    data = new unsigned char [imageSize];

    // Read the actual data from the file into the buffer
    fread(data,1,imageSize,file);

    //Everything is in memory now, the file can be closed
    fclose(file);

    // Create one OpenGL texture
    GLuint textureID;
    glGenTextures(1, &textureID);

    // "Bind" the newly created texture : all future texture functions will modify this texture
    glBindTexture(GL_TEXTURE_2D, textureID);

    // Give the image to OpenGL
	// image data is going into the "data" variable, that has been verified
    glTexImage2D(GL_TEXTURE_2D, 0,GL_RGB, width, height, 0, GL_BGR, GL_UNSIGNED_BYTE, data);

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

    return textureID;

}

void enableWriting(){
	glBindFramebuffer(GL_DRAW_FRAMEBUFFER, frameBuffer);
}

void disableWriting(){
	glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
}

void readPixel(GLint x, GLint y)
{
	glBindFramebuffer(GL_READ_FRAMEBUFFER, frameBuffer);
    glReadBuffer(GL_COLOR_ATTACHMENT0);

    glReadPixels(x, y, 1, 1, GL_RGB_INTEGER, GL_UNSIGNED_INT, &pixelData);

    glReadBuffer(GL_NONE);
    glBindFramebuffer(GL_READ_FRAMEBUFFER, 0);

}

void initPickingTexture(){

	int width = 640;
	int height = 480;

	//create the frame buffer
	glGenFramebuffers(1,&frameBuffer);
	glBindFramebuffer(GL_FRAMEBUFFER,frameBuffer);

	checkFrameBuffer();

	//creates the picking texture
	glGenTextures(1,&pickingTexture);
	glBindTexture(GL_TEXTURE_2D,pickingTexture);

	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

	glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32UI, width, height,
		0, GL_RGB_INTEGER, GL_INT, NULL);
    glFramebufferTexture2D(GL_DRAW_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, 
		pickingTexture, 0); 

	//creates the texture for the depth information
	glGenTextures(1, &depthTexture);
	glBindTexture(GL_TEXTURE_2D, depthTexture);
	
	checkFrameBuffer();

	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

	glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, width, height, 
                0, GL_DEPTH_COMPONENT, GL_FLOAT, NULL);
    glFramebufferTexture2D(GL_DRAW_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, 
		depthTexture, 0); 

	

    // Disable reading to avoid problems with older GPUs
    glReadBuffer(GL_NONE);

	checkFrameBuffer();

    // Restore the default framebuffer
    glBindTexture(GL_TEXTURE_2D, 0);
    glBindFramebuffer(GL_FRAMEBUFFER, 0);

	/* Unfortunately, this function kept returning the following error:
		GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT
	*/
	checkFrameBuffer();
}

void checkFrameBuffer(){
	// Verify that the FBO is correct
    GLenum Status = glCheckFramebufferStatus(GL_FRAMEBUFFER);

    if (Status != GL_FRAMEBUFFER_COMPLETE) {
        printf("\nFB error, status: 0x%x\n", Status);
    }

	if(Status == GL_FRAMEBUFFER_UNDEFINED){
		printf("\n GL_FRAMEBUFFER_UNDEFINED\n");
	}
	
	if(Status == GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT){
		printf("\n GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT\n");
	}

	if(Status == GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT){
		printf("\n GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT\n");
	}

	if(Status == GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER){
		printf("\n GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER\n");
	}

	if(Status == GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER){
		printf("\n GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER\n");
	}

	if(Status == GL_FRAMEBUFFER_UNSUPPORTED){
		printf("\n GL_FRAMEBUFFER_UNSUPPORTED\n");
	}

	if(Status == GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE){
		printf("\n GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE\n");
	}

	if(Status == GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE){
		printf("\n GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE\n");
	}

	if(Status == GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS){
		printf("\n GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS\n");
	}
}