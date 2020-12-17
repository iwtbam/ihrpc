package cn.iwtbam.ih.impl;

import cn.iwtbam.ih.interfaces.TargetService;

public class TargetServiceImpl implements TargetService {


    public TargetServiceImpl(){

    }

    @Override
    public int add(int a, int b) {
        return a + b;
    }
}
