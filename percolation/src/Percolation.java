public class Percolation {
    private final boolean[][] grid;
    private final int N;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF isFullUF;

    /***
     * create N-by-N grid, with all sites blocked
     * 
     * @param N
     *            size of the grid
     */
    public Percolation(int N) {
        this.N = N;
        grid = new boolean[N + 1][N + 1];
        uf = new WeightedQuickUnionUF(1 + N * N + 1);
        isFullUF = new WeightedQuickUnionUF(1 + N * N);
        for (int i = 1; i <= N; i++) {
            // Connect top
            union(0, i);
            // Connect bottom
            uf.union(N * N - i + 1, N * N + 1);
        }
    }

    private void checkBounds(int i, int j) {
        if (i < 1 || i > N)
            throw new IndexOutOfBoundsException("row index i out of bounds");
        if (j < 1 || j > N)
            throw new IndexOutOfBoundsException("column index j out of bounds");
    }

    private int xyTo1D(int i, int j) {
        return (i - 1) * N + j;
    }

    private void union(int p, int q) {
        uf.union(p, q);
        isFullUF.union(p, q);
    }

    /***
     * open site (row i, column j) if it is not already
     * 
     * @param i
     *            row
     * @param j
     *            column
     */
    public void open(int i, int j) {
        checkBounds(i, j);
        if (isOpen(i, j)) {
            return;
        }
        grid[i][j] = true;
        connectTop(i, j);
        connectBottom(i, j);
        connectLeft(i, j);
        connectRight(i, j);
    }

    private void connectRight(int i, int j) {
        if (j == N) {
            return;
        }

        if (isOpen(i, j + 1)) {
            int index = xyTo1D(i, j);
            int target = xyTo1D(i, j + 1);
            union(index, target);
        }
    }

    private void connectLeft(int i, int j) {
        if (j == 1) {
            return;
        }

        if (isOpen(i, j - 1)) {
            int index = xyTo1D(i, j);
            int target = xyTo1D(i, j - 1);
            union(index, target);
        }
    }

    private void connectBottom(int i, int j) {
        if (i == N) {
            return;
        }
        if (isOpen(i + 1, j)) {
            int index = xyTo1D(i, j);
            int target = xyTo1D(i + 1, j);
            union(index, target);
        }
    }

    private void connectTop(int i, int j) {
        if (i == 1) {
            return;
        }
        if (isOpen(i - 1, j)) {
            int index = xyTo1D(i, j);
            int target = xyTo1D(i - 1, j);
            union(index, target);
        }
    }

    /***
     * is site (row i, column j) open?
     * 
     * @param i
     *            row
     * @param j
     *            column
     * @return true is site is open
     */
    public boolean isOpen(int i, int j) {
        checkBounds(i, j);
        return grid[i][j];
    }

    /***
     * is site (row i, column j) full?
     * 
     * @param i
     * @param j
     * @return true if site is full
     */
    public boolean isFull(int i, int j) {
        if (!isOpen(i, j)) {
            return false;
        }
        int site = xyTo1D(i, j);
        return isFullUF.connected(0, site);
    }

    /***
     * does the system percolate?
     * 
     * @return true if the system percolates
     */
    public boolean percolates() {
        if (N == 1) {
            return isOpen(1, 1);
        }
        return uf.connected(0, N * N + 1);
    }
}