# jspmm

JSpMM provides matrix-matrix and matrix-vector multiplication for different matrix types for different backends 
like Multicore CPU, OpenCL and GridComputing based on GridGain in single precision floating point arithmetic.

The package support current matrix types:

* Dense
* Compressed Column Storage (CCS/CSC)
* Coordinate Storage Format (COO)
* CRS (Compressed Row Storage)
* Mutable COO
* Dense vector

Supported multiplication between the different matrix types are:

* (Dense, CCS, CRS, COO matrix) x (dense vector)
* (Dense matrix) x (CCS Matrix)
* (CRS matrix) x (CCS matrix) also COO result matrix
* more coming...

The (CRS matrix) x (CCS matrix) multiplication is based on the paper from [Sean Rose, GPU Sparse Matrix Multiplication with CUDA, 2013](https://www.cs.fsu.edu/research/projects/rose_report.pdf)

## OpenCL implementation

## Examples

    <some code stuff/>

## License

Free to use, just drop a note. I also would appreciate it if you give me credit where due.