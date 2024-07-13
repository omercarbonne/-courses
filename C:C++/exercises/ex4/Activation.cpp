#include "Activation.h"
#include "Matrix.h"
#include <cmath>
namespace activation
{
    Matrix relu(const Matrix& m)
    {
        Matrix new_mat(m.get_rows(), m.get_cols());
        for (int i = 0; i < m.get_cols()*m.get_rows(); i++)
        {
            if (m[i] >= 0)
            {
                new_mat[i] = m[i];
            }
        }
        return new_mat;
    }

    Matrix softmax(const Matrix& m)
    {
        Matrix new_mat(m.get_rows(), m.get_cols());
        float sum = 0.0;
        //change the value to exp() and calculate the sum.
        for (int i = 0; i < m.get_rows()*m.get_cols(); i++)
        {
            new_mat[i] = std::exp(m[i]);
            sum += std::exp(m[i]);
        }
        //divide all the values by the sum
        for (int i = 0; i < m.get_rows()*m.get_cols(); i++)
        {
                new_mat[i] *= (1/sum);
        }
        return new_mat;
    }



}


