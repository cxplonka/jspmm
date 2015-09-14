__kernel void crs_spmv_mult(__global float* y,
                     __global float* a,
                     __global float* x,
                    __global int* colIdx, 
                    __global int* rowPtr,
                     const int p) {

    int idx = get_global_id(0);

    if(idx >= p) {
        return;
    }

    float value = 0;
    for(int i = rowPtr[idx]; i < rowPtr[idx + 1]; i++) {        
        value += a[i] * x[colIdx[i]];
    }

    // back to global memory
    y[idx] = value;
}