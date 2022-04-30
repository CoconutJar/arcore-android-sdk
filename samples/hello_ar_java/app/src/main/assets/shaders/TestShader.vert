#version 300 es

const int MAX_JOINTS = 50;
const int MAX_WEIGHTS = 3;

uniform mat4 modelView;
uniform mat4 modelViewProjection;
uniform mat4 jointTransforms[MAX_JOINTS];

out vec3 v_ViewPosition;
out vec3 v_ViewNormal;
out vec2 v_TexCoord;

layout(location = 0) in vec3 a_Position;
layout(location = 1) in vec2 a_TextureCoords;
layout(location = 2) in vec3 a_Normals;
layout(location = 3) in ivec3 a_Joints;
layout(location = 4) in vec3 a_VertexWeights;


void main(void){

    vec4 totalLocalPos = vec4(0.0);
    vec4 totalNormal = vec4(0.0);

    for(int i = 0; i < MAX_WEIGHTS; i++){
        mat4 jointTransform = jointTransforms[a_Joints[i]];
        vec4 posePosition = jointTransform * vec4(a_Position,1.0);
        totalLocalPos += posePosition * a_VertexWeights[i];

        vec4 worldNormal = jointTransform * vec4(a_Normals,0.0);
        totalNormal += worldNormal * a_VertexWeights[i];
    }

    v_ViewPosition = (u_ModelView * a_Position).xyz;
    v_ViewNormal = normalize((u_ModelView * totalNormal));
    //v_ViewNormal = normalize((u_ModelView * vec4(a_Normal, 0.0)).xyz);
    v_TexCoord = a_TexCoord;
    gl_Position = u_ModelViewProjection * a_Position * totalLocalPos;
    //gl_Position = u_ModelViewProjection * a_Position


}
