# jspmm

JSpMM provides matrix-matrix and matrix-vector multiplication for different matrix types and different backends 
like Multicore CPU, OpenCL and GridComputing based on GridGain in single precision floating point arithmetic.

The package support current matrix types:

* Dense
* Compressed Column Storage [(CCS/CSC)](http://netlib.org/linalg/html_templates/node92.html)
* Coordinate Storage Format [(COO)](https://en.wikipedia.org/wiki/Sparse_matrix#Coordinate_list_.28COO.29)
* Compressed Row Storage [(CRS/CSR)](http://netlib.org/linalg/html_templates/node91.html)
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

Create matrices:
```java
// 5x5 matrix
static final float[] m0 = new float[]{
        0, 0, 3, 1, 0,
        2, 0, 0, 4, 0,
        0, 0, 2, 0, 0,
        0, 3, 0, 1, 0,
        4, 0, 2, 0, 3
    };

// dense matrix with 5 col and 5 row
DenseMatrix dense = new DenseMatrix(5, 5);

// create dense matrix from values
DenseMatrix dense = DenseMatrix.create(m0, 5);

// create CRS matrix from values, 0 elements will not be stored (sparse)
CRSMatrix crs = CRSMatrix.create(m0, 5);

// create CCS matrix from values, 0 elements will not be stored (sparse)
CCSMatrix ccs = CCSMatrix.create(m0, 5);

```

Multiply matrices (CPU):

```java
// multiply dense x dense matrix (single threaded)
dense0.multiply(dense0);

// multiply CRS x CCS matrix (single threaded) with result as COO matrix
MutableCOOMatrix coo = crs.multiply(ccs, MutableCOOMatrix.class);

// create SpMM context with default threaded executor
SpMM context = SpMM.create();

// concurrent CRS x CCS matrix with COO matrix as result
MutableCOOMatrix coo = context.multiply(crs, ccs);

```

Multiply matrices (OpenCL):

```java
// create OpenCL context with best device (GPU)
CL cl = CL.create();

// dense x dense matrix multiplication on the GPU
DenseFloatMatrix dense = cl.multiply(dense0, dense1);

// CRS x CCS matrix with COO matrix as result on the GPU
COOMatrix coo = cl.multiply(crs, ccs);

```

Multiply matrices on the GridGain cluster:

```java
// start or connect to your grid, for example
GridEntry grid = GridEntry.start();

// create SpMM context with the ExecutorService from the compute grid
SpMM context = SpMM.create(grid.getGrid().compute().executorService());

// dense x dense matrix multiplication on the GPU
DenseFloatMatrix dense = cl.multiply(dense0, dense1);

// CRS x CCS matrix with COO matrix as result on the compute grid
MutableCOOMatrix coo = spmm.multiply(crs, ccs);

```

## Note

Please note that the package is experimental and the API can change in the future.

## License

The MIT License (MIT)

Copyright (c) 2015 Christian Plonka

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
```