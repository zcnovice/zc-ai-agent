package com.zc.zcaiagent.dome.invok.Compoent_T.OutPut;

import reactor.core.publisher.Flux;

public class Flux_T {
    public static void main(String[] args) {
        //直接创建元素
        Flux<String> flux = Flux.just("Hello", "World", "!");

        //怎么输出flux
        //1.订阅
        flux.subscribe(System.out::println);
        System.out.println(flux);

    }
}
