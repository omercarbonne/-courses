#ifndef DENSE_H
#define DENSE_H
#include "Activation.h"
#include "Matrix.h"
typedef Matrix (*func_ptr)(const Matrix&);
// Insert Dense class here...

class Dense
{
private:
    Matrix weights;
    Matrix bias;
    func_ptr ActivationFunction;
public:
    /**
     * constructor
     * @param w Matrix which reps the weights
     * @param b Matrix which reps the bias
     * @param act_func the activation func
     */
    Dense(Matrix& w, Matrix& b, func_ptr act_func);
    /**
     * getter
     * @return Matrix of the weights
     */
    Matrix get_weights() const;
    /**
     * getter
     * @return Matrix of the bias
     */
    Matrix get_bias() const;
    /**
     * getter
     * @return func ptr of the activation func
     */
    func_ptr get_activation() const;
    /**
     * operate the dense on given Matrix
     * @param matrix Matrix
     * @return a new Matrix which is the result
     */
    Matrix operator()(const Matrix& matrix) const;
};

#endif //DENSE_H
