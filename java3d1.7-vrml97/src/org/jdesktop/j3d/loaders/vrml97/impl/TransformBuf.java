// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TransformBuf.java

package org.jdesktop.j3d.loaders.vrml97.impl;


// Referenced classes of package org.jdesktop.j3d.loaders.vrml97.impl:
//            Transform

public class TransformBuf
{

    TransformBuf()
    {
        array = new Transform[128];
        batchReady = true;
        size = 0;
    }

    void add(Transform newTrans)
    {
        if(size == array.length)
        {
            Transform newArray[] = new Transform[array.length + 128];
            System.arraycopy(array, 0, newArray, 0, array.length);
            array = newArray;
        }
        array[size++] = newTrans;
    }

    void startBatchLoading()
    {
        size = 0;
        batchReady = false;
    }

    void stopBatchLoading()
    {
        batchReady = true;
    }

    int size;
    Transform array[];
    boolean batchReady;
}
