#pragma OPENCL EXTENSION cl_khr_global_int32_base_atomics : enable

//source http://suhorukov.blogspot.de/2011/12/opencl-11-atomic-operations-on-floating.html
void atomic_add_local(volatile __global float *source, const float operand) {
    union {
        unsigned int intVal;
        float floatVal;
    } newVal;
 
    union {
        unsigned int intVal;
        float floatVal;
    } prevVal;
 
    do {
        prevVal.floatVal = *source;
        newVal.floatVal = prevVal.floatVal + operand;
    } while (atomic_cmpxchg((volatile __global unsigned int *)source, prevVal.intVal, newVal.intVal) != prevVal.intVal);
}

__kernel void coo_spmv_mult(__global float* y,
                     __global float* a,
                     __global float* x,
                    __global int* rowIdx,
                    __global int* colIdx,
                     const int nz) {

    int idx = get_global_id(0);

    //out of range of non zero elements
    if(idx >= nz) {
        return;
    }

    int row = rowIdx[idx];
    int col = colIdx[idx];
    float value = a[idx];

    //slow atomic add operation to global memory
    atomic_add_local(&y[row], value * x[col]);
}