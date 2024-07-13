#ifndef ACTIVATION_H
#define ACTIVATION_H
#include "Matrix.h"
// Insert Activation class here...
namespace activation{
    /**
     * change all the negative values in the matrix to 0 and create new one
     * @param m Matrix
     * @return a new Matrix with the new values
     */
    Matrix relu(const Matrix& m);
    /**
     * make softmax to the matrix and create another one
     * @param m Matrix
     * @return a new Matrix with the new values
     */
    Matrix softmax(const Matrix& m);
}

#endif //ACTIVATION_H