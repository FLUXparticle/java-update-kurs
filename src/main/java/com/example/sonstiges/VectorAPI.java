import jdk.incubator.vector.*;

import java.util.*;

static final VectorSpecies<Float> SPECIES = FloatVector.SPECIES_PREFERRED;

void vectorComputation(float[] a, float[] b, float[] c) {
    int i = 0;
    System.out.println("length = " + SPECIES.length());
    int upperBound = SPECIES.loopBound(a.length);
    System.out.println("upperBound = " + upperBound);
    for (; i < upperBound; i += SPECIES.length()) {
        // FloatVector va, vb, vc;
        var va = FloatVector.fromArray(SPECIES, a, i);
        var vb = FloatVector.fromArray(SPECIES, b, i);
        var vc = va.mul(va)
                .add(vb.mul(vb))
                .neg();
        vc.intoArray(c, i);
    }
    for (; i < a.length; i++) {
        c[i] = (a[i] * a[i] + b[i] * b[i]) * -1.0f;
    }
}

float[] vector(float... values) {
    return values;
}

public void main() {
    float[] result = new float[4];
    vectorComputation(vector(1, 2, 3, 4), vector(4, 5, 6, 8), result);
    System.out.println(STR."result = \{Arrays.toString(result)}");
}
