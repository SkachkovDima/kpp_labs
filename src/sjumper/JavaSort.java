package sjumper;

class JavaSort {
  void sort(int[] xs, String[] ss) {
    sort(xs, ss, 0, xs.length - 1);
  }

  private void sort(int[] xs, String[] ss, int l, int r) {
    int pivot = xs[(l + r) / 2];
    int a = l;
    int b = r;
    while (a <= b) {
      while (xs[a] > pivot) {
        a = a + 1;
      }
      while (xs[b] < pivot) {
        b = b - 1;
      }
      if (a <= b) {
        swap(xs, ss, a, b);
        a = a + 1;
        b = b - 1;
      }
    }
    if (l < b) {
      sort(xs, ss, l, b);
    }
    if (b < r) {
      sort(xs, ss, a, r);
    }
  }

  private void swap(int[] xs, String[] ss, int i, int j) {
    int t = xs[i];
    xs[i] = xs[j];
    xs[j] = t;
    String s = ss[i];
    ss[i] = ss[j];
    ss[j] = s;
  }
}
