#dream-guice
该项目维护guice的拓展，主要实现了如下几个特性：

###特性一
guice-lifecycle:考虑guice应用到其他容器时候，可以快速与容器的生命周期进行桥接，为guice提供生命周期特性。该实现参考了ninjaframework的生命周期实现。

###特性二
guice-jpa:考虑guice应用到其他容器的时候，可以快速应用dao的封装以及基于aop提供事务统一管理能力

###特性三
guice-disconf:提供分布式系统配置管理