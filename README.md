# AXON SAGA CHOREOGRAPHY EXAMPLES

> https://github.com/dashsaurabh/saga-pattern-axon-spring-boot-sample
> https://progressivecoder.com/saga-pattern-implementation-with-axon-and-spring-boot-part-1/

- Ví dụ về transaction và rollback:

> https://github.com/pivotalsoftware/ESarch

### Draw diagram support for markdown: Markdown editor

- Idea extensions:

> install plantuml file type for idea: File | Settings | Languages & Frameworks | Markdown | PlanUml

- Draw diagram:

> https://plantuml.com/sequence-diagram

## I. Biểu đồ luồng order trong example

```plantuml
actor client #yellow
box orderSvc #LightBlue
    client -> orderSvc ++: 1: call Api order a product
        ' com.mbbank.ctv.orderservice.controllers.OrderCommandController.createOrder
    note right of client: Clent gọi api /api/orders \n và truyền lên thông tin mặt hàng
    orderSvc -> service --++: 2: commandGateway: CreateOrderCommand
        ' com.mbbank.ctv.orderservice.aggregates.OrderAggregate.OrderAggregate(com.mbbank.ctv.messages.commands.CreateOrderCommand)
    note right of service: orderService tạo command \n rồi gọi commandGateway.send()
    service -> aggregate --++: 3: @CommandHandler: CreateOrderCommand
    note right of service: tập hợp bắt được event \n rồi lưu trạng thái command \n OrderAggregate(CreateOrderCommand)
    par handler CreateOrderCommand
        aggregate -> aggregate --++: 4.1: @EventSourcingHandler: OrderCreatedEvent \n (save state of data)
            ' com.mbbank.ctv.orderservice.aggregates.OrderAggregate.on(com.mbbank.ctv.messages.events.OrderCreatedEvent)
        aggregate -> OrderHandler --: 4.2: 5@EventHandler: OrderCreatedEvent: \n insert order to database
            ' com.mbbank.ctv.query.OrderHandler.on(com.mbbank.ctv.messages.events.OrderCreatedEvent)
    end
    database Database as dbOrder #a8a8a8
    OrderHandler -> dbOrder --++: 5.1: Lưu trạng thái OrderCreatedEvent
        ' com.mbbank.ctv.orderservice.aggregates.OrderAggregate.on(com.mbbank.ctv.messages.events.OrderRecordSavedEvent)
    OrderHandler -> aggregate --++: 5.1: send event OrderRecordSavedEvent \n Đã lưu order vào db
    par  save stage and call new command
        aggregate -> aggregate --++: 6.1: Lifecycle.apply: \n @EventSourcedHandler: OrderRecordSavedEvent
            ' com.mbbank.ctv.orderservice.aggregates.OrderAggregate.on(com.mbbank.ctv.messages.events.OrderRecordSavedEvent)
        aggregate -> saga --++: 6.2: @StartSaga,@Transactional,@SagaEventHandler: \n OrderRecordSavedEvent
            ' com.mbbank.ctv.orderservice.sagas.SagaManagement.handle(com.mbbank.ctv.messages.events.OrderRecordSavedEvent)
    end
end box
box orderSvc #LightGreen

end box
```

