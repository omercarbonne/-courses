#include "Matrix.h"
#include <iostream>
#include <cmath>

#define PRINT_LEVEL 0.1

Matrix::Matrix(int rows, int cols) {
    if((rows < 1) || (cols < 1))
    {
        throw std::length_error("The dims has to be positive int");
    }
    dims.rows = rows;
    dims.cols = cols;
    arr = new float[dims.rows*dims.cols];
    for (int i = 0; i<dims.rows*dims.cols; i++)
    {
        arr[i] = 0;
    }
}
Matrix::Matrix() {
    dims.rows = 1;
    dims.cols = 1;
    arr = new float[1];
    arr[0] = 0;
}
Matrix::Matrix(const Matrix &matrix) {
    dims.rows = matrix.dims.rows;
    dims.cols = matrix.dims.cols;
    arr = new float[dims.rows*dims.cols];
    for (int i = 0; i < dims.rows*dims.cols; i++)
    {
       arr[i] =  matrix.arr[i];
    }
}
Matrix::~Matrix() {
    delete[] arr;
}

int Matrix::get_rows() const {
    return dims.rows;
}

int Matrix::get_cols() const {
    return dims.cols;
}

Matrix& Matrix::transpose() {
    //creates new arr which will replace the current
    float* new_arr  = new float[dims.rows*dims.cols];
    //change it to transpose
    for (int n = 0; n< dims.rows*dims.cols; n++)
    {
        int i = n/dims.rows;
        int j = n%dims.rows;
        new_arr[n] = arr[(dims.cols*j) + i];

    }
    //swap the dims
    int temp = dims.rows;
    dims.rows = dims.cols;
    dims.cols = temp;
    //replace arr
    delete[] arr;
    arr = new_arr;
    return *this;
}

Matrix& Matrix::vectorize() {
    dims.rows = dims.rows*dims.cols;
    dims.cols = 1;
    return *this;
}

void Matrix::plain_print() const {
    for (int i = 0; i<dims.rows; i++)
    {
        for (int j = 0; j<dims.cols; j++)
        {
            std::cout << (*this)(i,j) << " ";
        }
        std::cout << std::endl;
    }
}

Matrix Matrix::dot(const Matrix &matrix) const{
    //check if the matrices fits
    if ((dims.cols != matrix.dims.cols)||(dims.rows != matrix.dims.rows))
    {
        throw std::length_error("The matrices arent fit");
    }
    Matrix new_mat(dims.rows,dims.cols);
    for (int i = 0; i < new_mat.dims.rows; i++)
    {
        for (int j = 0; j < new_mat.dims.cols; j++)
        {
            new_mat(i,j) = (*this)(i,j)*matrix(i,j);
        }
    }
    return new_mat;
}

float Matrix::norm() const {
    float sum = 0;
    for (int i = 0; i < dims.rows*dims.cols; i++)
    {
        sum += arr[i]*arr[i];
    }
    return std::sqrt(sum);
}

float Matrix::argmax() const {
    float res = arr[0];
    // find max
    for (int i = 1; i < dims.rows*dims.cols; i++)
    {
        if (res < arr[i])
        {
            res = arr[i];
        }
    }
    return res;
}

float Matrix::sum() const {
    float sum = 0;
    for (int i = 0; i < dims.rows*dims.cols; i++)
    {
        sum += arr[i];
    }
    return sum;
}

Matrix operator+(const Matrix& a, const Matrix& b)
{
    //check if the matrices fits
    if ((a.dims.cols != b.dims.cols)||(a.dims.rows != b.dims.rows))
    {
        throw std::length_error("The matrices arent fit");
    }
    //create a new one which will be the result
    Matrix new_mat(a.dims.rows, a.dims.cols);
    for (int i = 0; i < a.dims.rows*a.dims.cols; i++)
    {
        new_mat.arr[i] = a.arr[i] + b.arr[i];
    }
    return new_mat;
}

Matrix &Matrix::operator=(const Matrix& matrix) {
    //check if it the same matrix
    if(this == &matrix)
    {
        return *this;
    }
    delete[] arr;
    dims.rows = matrix.dims.rows;
    dims.cols = matrix.dims.cols;
    //creates new arr with right dims
    arr = new float[dims.rows * dims.cols];
    for (int i = 0; i < dims.rows*dims.cols; i++)
    {
        arr[i] =  matrix.arr[i];
    }
    return *this;
}

Matrix operator*(const Matrix& a, const Matrix& b)
{
    //check if the matrices fits
    if (a.dims.cols != b.dims.rows)
    {
        throw std::length_error("The matrices arent fit");
    }
    //create new matrix which will be the result
    Matrix new_mat(a.dims.rows,b.dims.cols);
    for (int i = 0; i < new_mat.dims.rows; i++)
    {
        for (int j = 0; j < new_mat.dims.cols; j++)
        {
            float res = 0;
            for (int n = 0; n < a.dims.cols; n++)
            {
                res += a(i,n)*b(n,j);
            }
            new_mat(i,j) = res;
        }
    }
    return new_mat;
}
Matrix operator*(const Matrix& m, float c)
{
    //create new matrix which will be the result
    Matrix new_mat(m.dims.rows, m.dims.cols);
    for (int i = 0; i < m.dims.rows*m.dims.cols; i++)
    {
        new_mat.arr[i] = m.arr[i]*c;
    }
    return new_mat;
}
Matrix operator*(float c, const Matrix& m)
{
    //create new matrix which will be the result
    Matrix new_mat(m.dims.rows, m.dims.cols);
    for (int i = 0; i < m.dims.rows*m.dims.cols; i++)
    {
        new_mat.arr[i] = m.arr[i]*c;
    }
    return new_mat;
}

float& Matrix::operator()(int rows, int cols) {
    //check if the index ok
    if((rows >= dims.rows)||(cols>=dims.cols))
    {
        throw std::out_of_range("The given index is out of range");
    }
    return arr[rows*dims.cols + cols];
}

float Matrix::operator()(int rows, int cols) const {
    //check if the index ok
    if((rows >= dims.rows)||(cols>=dims.cols))
    {
        throw std::out_of_range("The given index is out of range");
    }
    return arr[rows*dims.cols + cols];
}

float &Matrix::operator[](int index) {
    //check if the index ok
    if(index > dims.rows*dims.cols-1)
    {
        throw std::out_of_range("The given index is out of range");
    }
    return arr[index];
}

float Matrix::operator[](int index) const {
    //check if the index ok
    if(index > dims.rows*dims.cols-1)
    {
        throw std::out_of_range("The given index is out of range");
    }
    return arr[index];
}

std::ostream& operator<<(std::ostream& s, const Matrix& m)
{
    for (int i = 0; i < m.dims.rows; i++)
    {
        for (int j = 0; j < m.dims.cols; j++)
        {
            if (m(i,j) > PRINT_LEVEL)
            {
                s << "**";
            }
            else
            {
                s << "  ";
            }
        }
        s << std::endl;
    }
    return s;
}



std::istream& operator>>(std::istream& s, Matrix& m)
{
    size_t m_len = sizeof(float)*m.dims.rows*m.dims.cols;
    s.read((char*)(m.arr), m_len);
    size_t input = s.gcount();
    //check if the length is enough to fill the matrix
    if(input < m_len)
    {
        throw std::runtime_error("Stream Length is invalid");
    }
    if(!s)
    {
        throw std::runtime_error("The file is not valid");
    }
    return s;

}

Matrix& Matrix::operator+=(const Matrix& matrix) {
    //check if the matrices is ok
    if((dims.rows != matrix.dims.rows)||(dims.cols!=matrix.dims.cols))
    {
        throw std::length_error("The matrices arent fit");
    }
    for(int i = 0; i < dims.rows*dims.cols; i++)
    {
        arr[i] += matrix.arr[i];
    }
    return *this;
}