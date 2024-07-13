#include "MlpNetwork.h"

#define ARR_SIZE 10

 MlpNetwork::MlpNetwork(Matrix w[MLP_SIZE], Matrix b[MLP_SIZE]):
         layer_1(w[0], b[0], activation::relu),
         layer_2(w[1], b[1], activation::relu),
         layer_3(w[2], b[2], activation::relu),
         layer_4(w[3], b[3], activation::softmax) {}

digit MlpNetwork::operator()(Matrix &matrix) const {
    matrix.vectorize();
    Matrix res = layer_4(layer_3(layer_2(layer_1(matrix))));
    digit d;
    d.value = 0;
    d.probability = 0;
    //check which is the highest probability
    for (int i = 0; i<ARR_SIZE; i++)
    {
        if(res[i] > d.probability)
        {
            d.value = i;
            d.probability = res[i];
        }
    }
    return d;
}