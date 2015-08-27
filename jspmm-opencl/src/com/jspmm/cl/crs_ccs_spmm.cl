#pragma OPENCL EXTENSION cl_khr_global_int32_base_atomics : enable

__kernel void crs_ccs_spmm_nnz(__global float* a,
                     __global float* b,
                    __global int* aColIdx, 
                    __global int* aRowPtr,
                    __global int* bRowIdx, 
                    __global int* bColPtr,
                    const int p,
                    const int r,
                    __global int* nnz) {

    int rowA = get_global_id(0);
    int colB = get_global_id(1);
    
    // pad matrix elements by zero or check
    if(rowA >= p || colB >= r) {
        return;
    }

    float value = 0;
    int cidx;
    
    // each nonzero in A row
    for (int i = aRowPtr[rowA]; i < aRowPtr[rowA + 1]; i++) {        
        cidx = aColIdx[i]; //memory access pattern not gpu friendly
        // each nonzero in B column
        for (int j = bColPtr[colB]; j < bColPtr[colB + 1]; j++) {
            if(cidx == bRowIdx[j]) {
                value += a[i] * b[j];
                break;
            }
        }
    }

    // increment atomic, maybe local reduction better
    if(value != 0) {
        atom_inc(&nnz[0]);
    }
}

__kernel void crs_ccs_spmm_mult(__global float* a,
                    __global float* b,
                    __global float* c,
                    __global int* aColIdx,
                    __global int* aRowPtr,
                    __global int* bRowIdx,
                    __global int* bColPtr,
                    __global int* cRowIdx,
                    __global int* cColIdx,
                    const int p,
                    const int r,
                    __global int* nnz) {

    int rowA = get_global_id(0);
    int colB = get_global_id(1);
    
    //pad matrix elements by zero or check
    if(rowA >= p || colB >= r) {
        return;
    }

    float value = 0;
    int cidx;
    
    //each nonzero in A row
    for (int i = aRowPtr[rowA]; i < aRowPtr[rowA + 1]; i++) {        
        cidx = aColIdx[i]; //memory access pattern not gpu friendly
        //each nonzero in B column
        for (int j = bColPtr[colB]; j < bColPtr[colB + 1]; j++) {
            if(cidx == bRowIdx[j]) {
                value += a[i] * b[j];
                break;
            }
        }
    }

    //add to COO matrix
    if(value != 0) {
        int idx = atom_inc(&nnz[0]);
        cRowIdx[idx] = rowA;
        cColIdx[idx] = colB;
        c[idx] = value;
    }
}