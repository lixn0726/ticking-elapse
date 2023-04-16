# Design

## Core Components
### module: wheel timer

### module: cursor
### module: config

### module: timeout
````
public interface Timeout {
    long getDelayed();
    boolean isRepeatable();
    void perform();
}
````
Timeout是定时消息/任务的封装/抽象。

## Usage

---
1. WheelTimerBuilder.single()
2. WheelTimerBuilder.single(WheelTimerConfig config)
3. WheelTimerBuilder.multi().haveUpper()...
4. WheelTimerBuilder.multi(WheelTimerConfig config).haveUpper(WheelTimerConfig config)...
---
通过 `WheelTimerBuilder`生成默认/自定义的时间轮，single则生成单层，multi则是层级时间轮
在代码中，不论是单层还是多层，默认的实现都是同一个类
单层还是多层的体现只在 `提交超过maxDuration的Timeout`的处理策略有所不同

在常见的单层时间轮中，对于过久的Timeout，一般会在管理Timeout的封装类中定义一个Round属性，Round=N表示Timeout超过了多少个maxDuration
这样带来的问题是任务列表的维护成本，以及获取到期Timeout时的遍历成本，因为在寻找存放Timeout的bucket position时，只考虑delayed % maxDuration
的结果，所以不同Round的Timeout会被放在同一个bucket，然后到期时，则要去找到Round=0的Timeout，则必须全部Timeout都遍历到，带来了比较大的成本。
并且，在Timeout很多的时候，如果tickDuration设计不合理，很容易导致bucket中的Timeout列表过长，Timeout列表的实现也比较难以选择，并且CRUD的成本也比较大。

而对于多层次的时间轮，目前也已经在比较多的组件中得到使用，比如Kafka、QMQ等。
多层次的时间轮和单层时间轮的唯一区别就在于Round属性，多层次的时间轮bucket不会维护Round属性，到期后直接取出bucket的所有Timeout执行即可。

## API design
个人认为，时间轮组件应该尽量做到即插即用，因为本身就是一个数据结构而已，没有必要提供过多的定制化，但是也需要保留接口，
对于时间轮，大家关注的点可能在于
1. 时间轮的启停
2. 任务提交

只有这两个功能是比较常见的，那么只开了这两方面的API，默认提供仿时钟的时间轮，或者可以让用户自定义参数。
