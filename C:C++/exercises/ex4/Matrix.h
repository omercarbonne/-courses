// Matrix.h
#ifndef MATRIX_H
#define MATRIX_H
#include <ostream>

/**
 * @struct matrix_dims
 * @brief Matrix dimensions container. Used in MlpNetwork.h and main.cpp
 */
typedef struct matrix_dims
{
	int rows, cols;

} matrix_dims;

// Insert Matrix class here...

class Matrix
{
    float *arr;
    matrix_dims dims;

    public:
    /**
     * constructor
     * @param rows int
     * @param cols int
     */
        Matrix(int rows, int cols);
        /**
         * default constructor
         */
        Matrix();
        /**
         * constructor
         * @param matrix Matrix
         */
        Matrix(const Matrix &matrix);
        /**
         * destructor
         */
        ~Matrix();
        /**
         * getter
         * @return int of the rows number
         */
        int get_rows() const;
        /**
         * getter
         * @return int of the columns number
         */
        int get_cols() const;
        /**
         * transpose the matrix.
         * @return ref to the matrix.
         */
        Matrix& transpose();
        /**
         * change the matrix into a matrix with only one column an n rows.
         * @return ref to the matrix.
         */
        Matrix& vectorize();
        /**
         * prints the matrix.
         */
        void plain_print() const;
        /**
         *
         * @param matrix: another Matrix with the same dims.
         * @return new matrix which is the result of their smart mult.
         */
        Matrix dot(const Matrix &matrix) const;
        /**
         * calc the norm of the matrix
         * @return float
         */
        float norm() const;
        /**
         * find and return the max num in the matrix
         * @return float
         */
        float argmax() const;
        /**
         * calc the sum of all the numbers in the matrix
         * @return float
         */
        float sum() const;
        /**
         * calc the sum of the Matrices
         * @param a Matrix
         * @param b Matrix
         * @return new Matrix which is the sum of the other two
         */
        friend Matrix operator+(const Matrix& a, const Matrix& b);
        /**
         * assign one Matrix to another
         * @param b Matrix
         * @return the Matrix assigned
         */
        Matrix& operator=(const Matrix& b);
        /**
         * Matrices mult
         * @param a Matrix
         * @param b Matrix
         * @return a new Matrix which is the mult of the other two
         */
        friend Matrix operator*(const Matrix& a, const Matrix& b);
        /**
         * Matrix mult by scalar
         * @param m Matrix
         * @param c float scalar
         * @return a new Matrix which is the result
         */
        friend Matrix operator*(const Matrix& m, float c);
    /**
     * Matrix mult by scalar
     * @param c float scalar
     * @param m Matrix
     * @return a new Matrix which is the result
     */
        friend Matrix operator*(float c, const Matrix& m);
        /**
         * get the value in a given location from the matrix. read access.
         * @param rows
         * @param cols
         * @return float value
         */
        float& operator()(int rows, int cols);
    /**
     * get the value in a given location from the matrix. write access.
     * @param rows
     * @param cols
     * @return float value
     */
        float operator()(int rows, int cols) const;
        /**
         * gets the value in the given index from the matrix.
         * write access.
         * @param index
         * @return
         */
        float& operator[](int index);
    /**
     * gets the value in the given index from the matrix.
     * read access.
     * @param index
     * @return
     */
        float operator[](int index) const;
        /**
         * stream all the values in the matrix to represent a number
         * @param s ostream
         * @param m Matrix
         * @return ostream
         */
        friend std::ostream& operator<<(std::ostream& s, const Matrix& m);
        /**
         * stream a given input into a Matrix.
         * the input must be long enough to fill the matrix.
         * @param s istream
         * @param m Matrix
         * @return istream
         */
        friend std::istream& operator>>(std::istream& s, Matrix& m);
        /**
         * operator += to add to one matrox the other.
         * @param matrix
         * @return a ref to the first matrix.
         */
        Matrix& operator +=(const Matrix& matrix);

};
#endif //MATRIX_H