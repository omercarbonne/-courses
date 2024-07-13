//MlpNetwork.h

#ifndef MLPNETWORK_H
#define MLPNETWORK_H

#include "Dense.h"
#include "Activation.h"

#define MLP_SIZE 4

/**
 * @struct digit
 * @brief Identified (by Mlp network) digit with
 *        the associated probability.
 * @var value - Identified digit value
 * @var probability - identification probability
 */
typedef struct digit {
	unsigned int value;
	float probability;
} digit;

const matrix_dims img_dims = {28, 28};
const matrix_dims weights_dims[] = {{128, 784},
									{64,  128},
									{20,  64},
									{10,  20}};
const matrix_dims bias_dims[] = {{128, 1},
								 {64,  1},
								 {20,  1},
								 {10,  1}};

// Insert MlpNetwork class here...
class MlpNetwork{
private:
   Dense layer_1;
   Dense layer_2;
   Dense layer_3;
   Dense layer_4;

public:
    /**
     * constructor
     * @param w arr of Matrices
     * @param b arr of Matrices
     */
    MlpNetwork(Matrix w[MLP_SIZE], Matrix b[MLP_SIZE]);
    /**
     * operate the network on a given Matrix
     * @param matrix Matrix
     * @return the result number
     */
    digit operator()(Matrix& matrix) const;


};

#endif // MLPNETWORK_H