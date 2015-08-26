__kernel void dense_spmm_mult(__global float* c,
                     __global float* a,
                     __global float* b,
                     const int p,
                     const int r,
                     const int q) {

    int i = get_global_id(0);
    int j = get_global_id(1);
    
    //pad matrix elements by zero or check
    if(i >= p || j >= r) {
        return;
    }

    float value = 0;
    for (int k = 0; k < q; k++) {        
        value += a[i * q + k] * b[k * r + j];
    }
    c[i * r + j] = value;
}