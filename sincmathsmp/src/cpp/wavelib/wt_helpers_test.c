#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include "wavelib.h"
#include "wt_helpers.h"

int main() {

    FILE *ifp;
    double temp[1200];
    int i = 0;

    ifp = fopen("example_signal.txt", "r");
    i = 0;
    if (!ifp) {
        printf("Cannot Open File");
        exit(100);
    }
    while (!feof(ifp)) {
        fscanf(ifp, "%lf \n", &temp[i]);
        i++;
    }

    fclose(ifp);

    int signal_length = 585;

    double *input = (double *) malloc(sizeof(double) * signal_length);
    double *output = (double *) malloc(sizeof(double) * signal_length);

    for (i = 0; i < signal_length; ++i) {
        input[i] = temp[i];
    }

    diff_cwtft(input, output, signal_length, 16.0, 0.01);

    for (i = 0; i < signal_length; ++i) {
        printf("%f\n", output[i]);
    }

    free(input);
    free(output);

    return 0;
}