#include "Dense.h"

Dense::Dense(Matrix& w, Matrix& b, func_ptr act_func) {
    weights = w;
    bias = b;
    ActivationFunction = act_func;
}

Matrix Dense::get_weights() const {
    return weights;
}

Matrix Dense::get_bias() const {
    return bias;
}

func_ptr Dense::get_activation() const {
    return ActivationFunction;
}

Matrix Dense::operator()(const Matrix &vec) const {
    Matrix new_mat = weights*vec;
    new_mat = new_mat + bias;
    new_mat = ActivationFunction(new_mat);
    return new_mat;
}