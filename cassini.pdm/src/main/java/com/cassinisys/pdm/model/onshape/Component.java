package com.cassinisys.pdm.model.onshape;

import lombok.Data;

import java.io.Serializable;

@Data
public class Component implements Serializable {
    private String name;
    private int cassiniId;
    private Double[] transform;

    public Double[] convertToOnshape() {
        //return convertSwxTransformToOnshapeTransform();
        return convertSwxDMTransformToOnshapeTransform();
    }

    public Double[] convertSwxTransformToOnshapeTransform() {
        if(this.transform != null) {
            Double[] on = new Double[this.transform.length];
            //1st row
            on[0] = this.transform[0];
            on[1] = this.transform[1];
            on[2] = this.transform[2];
            on[3] = this.transform[9];

            //2nd row
            on[4] = this.transform[3];
            on[5] = this.transform[4];
            on[6] = this.transform[5];
            on[7] = this.transform[10];

            //3rd row
            on[8] = this.transform[6];
            on[9] = this.transform[7];
            on[10] = this.transform[8];
            on[11] = this.transform[11];

            //4th row
            on[12] = 0.0;
            on[13] = 0.0;
            on[14] = 0.0;
            on[15] = 1.0;
            return on;
        }

        return null;
    }

    public Double[] convertSwxDMTransformToOnshapeTransform() {
        if(this.transform != null) {
            Double[] on = new Double[this.transform.length];
            //1st row
            on[0] = this.transform[0];
            on[1] = this.transform[4];
            on[2] = this.transform[8];
            on[3] = this.transform[12];

            //2nd row
            on[4] = this.transform[1];
            on[5] = this.transform[5];
            on[6] = this.transform[9];
            on[7] = this.transform[13];

            //3rd row
            on[8] = this.transform[2];
            on[9] = this.transform[6];
            on[10] = this.transform[10];
            on[11] = this.transform[14];

            //4th row
            on[12] = 0.0;
            on[13] = 0.0;
            on[14] = 0.0;
            on[15] = 1.0;
            return on;
        }

        return null;
    }

    public static void main(String[] args) {
        Double[] sw = new Double[]{-0.4522721486284359,
                0.891879982719098,
                -3.2490784445556044E-21,
                -0.89187998271909785,
                -0.45227214862843551,
                1.2120554555904968E-20,
                8.964932452780983E-21,
                8.5019531923193643E-21,
                0.99999999999999989,
                0.023248847139455343,
                0.020450884828308966,
                0.036613595475443662,
                1.0,
                0.0,
                0.0,
                0.0};
        Double[] dm = new Double[]{-0.4522721486284359,
                0.891879982719098,
                -3.2490784445556041E-21,
                0.0,
                -0.89187998271909785,
                -0.45227214862843551,
                1.212055455590497E-20,
                0.0,
                8.964932452780983E-21,
                8.5019531923193643E-21,
                0.99999999999999989,
                0.0,
                0.023248847139455339,
                0.020450884828308969,
                0.036613595475443662,
                1.0};

        Component cSw = new Component();
        cSw.setTransform(sw);
        Double[] tSw = cSw.convertSwxTransformToOnshapeTransform();

        Component cDm = new Component();
        cDm.setTransform(dm);
        Double[] tDm = cDm.convertSwxDMTransformToOnshapeTransform();

        System.out.println(tSw);
        System.out.println(tDm);
    }
}
