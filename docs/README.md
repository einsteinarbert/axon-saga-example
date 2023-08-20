# AXON SAGA CHOREOGRAPHY EXAMPLES

> https://github.com/dashsaurabh/SagaOrder-pattern-axon-spring-boot-sample
> https://progressivecoder.com/SagaOrder-pattern-implementation-with-axon-and-spring-boot-part-1/

- Ví dụ về transaction và rollback:

> https://github.com/pivotalsoftware/ESarch

### Draw diagram support for markdown: Markdown editor

- Idea extensions:

> install plantuml file type for idea: File | Settings | Languages & Frameworks | Markdown | PlanUml

- Export HTML: Select .md file first then:
> Tools | Markdown tools | Export html

- Draw diagram:

> https://plantuml.com/sequence-diagram

## I. Biểu đồ luồng order trong example

```plantuml
' skinparam style strictuml
actor client #yellow
box orderSvc #LightBlue
    client -> orderSvc ++: 1: call Api order a product
        ' com.mbbank.ctv.orderservice.controllers.OrderCommandController.createOrder
    note right of client: Clent gọi api /api/orders \n và truyền lên thông tin mặt hàng
    orderSvc -> service --++: 2: commandGateway: CreateOrderCommand
        ' com.mbbank.ctv.orderservice.aggregates.OrderAggregate.OrderAggregate(com.mbbank.ctv.messages.commands.CreateOrderCommand)
    note right of service: orderService tạo command \n rồi gọi commandGateway.send()
    service -> aggregate_order --++: 3: @CommandHandler: CreateOrderCommand
    note right of service: tập hợp bắt được event \n rồi lưu trạng thái command \n OrderAggregate(CreateOrderCommand)
    par handler CreateOrderCommand
        aggregate_order -> aggregate_order --++: 4.1: @EventSourcingHandler: OrderCreatedEvent \n (save state of data)
            ' com.mbbank.ctv.orderservice.aggregates.OrderAggregate.on(com.mbbank.ctv.messages.events.OrderCreatedEvent)
        aggregate_order -> OrderHandler --++: 4.2: 5@EventHandler: OrderCreatedEvent: \n insert order to database
            ' com.mbbank.ctv.query.OrderHandler.on(com.mbbank.ctv.messages.events.OrderCreatedEvent)
    end
    database repo_order #a8a8a8
    OrderHandler -> repo_order --++: 5.1: Lưu trạng thái OrderCreatedEvent
        ' com.mbbank.ctv.orderservice.aggregates.OrderAggregate.on(com.mbbank.ctv.messages.events.OrderRecordSavedEvent)
    repo_order -> aggregate_order --++: 5.2: send event OrderRecordSavedEvent \n Đã lưu order vào db
    par  save stage and call new command
        aggregate_order -> aggregate_order --++: 6.1: Lifecycle.apply: \n @EventSourcedHandler: OrderRecordSavedEvent
            ' com.mbbank.ctv.orderservice.aggregates.OrderAggregate.on(com.mbbank.ctv.messages.events.OrderRecordSavedEvent)
        aggregate_order -> SagaOrder --++: 6.2: @StartSaga,@Transactional,@SagaEventHandler: \n OrderRecordSavedEvent
            ' com.mbbank.ctv.orderservice.sagas.SagaManagement.handle(com.mbbank.ctv.messages.events.OrderRecordSavedEvent)
    end
end box
box paymentSvc #ccffee
    SagaOrder -> paymentSvc --++: 7: Handler CreateInvoiceCommand \n nhận command tạo hóa đơn
    ' com.mbbank.ctv.paymentservice.aggregates.InvoiceAggregate#InvoiceAggregate(com.mbbank.ctv.messages.commands.CreateInvoiceCommand)
    par save stage và tạo event mới
        paymentSvc -> aggregate_payment --++: 8.1: apply InvoiceCreatedEvent \n handle và lưu state event
        ' com.mbbank.ctv.paymentservice.aggregates.InvoiceAggregate.on
        paymentSvc -> paymentSaga --++: 8.2: fire event InvoiceCreatedEvent
        ' com.mbbank.ctv.paymentservice.sagas.SagaManagement#handle
    end
end box
box shiperSvc #69ebf5
    paymentSaga -> shiperSvc --++: 9: payment service send \n Command CreateShippingCommand \n to Shipping service
    ' com.mbbank.ctv.shippingservice.aggregates.ShippingAggregate.ShippingAggregate(com.mbbank.ctv.messages.commands.CreateShippingCommand)
    shiperSvc -> ShipAggregate --++: 10: handle CreateShippingCommand
    ' com.mbbank.ctv.shippingservice.aggregates.ShippingAggregate.ShippingAggregate(com.mbbank.ctv.messages.commands.CreateShippingCommand)
    par
        ShipAggregate -> ShipAggregate --++: 11.1: Fire OrderShippedEvent
        ' com.mbbank.ctv.shippingservice.aggregates.ShippingAggregate.on(OrderShippedEvent orderShippedEvent)
        ShipAggregate -> SagaOrder --++: 11.2: Handler OrderShippedEvent: \n đã ship hàng thành công
        ' com.mbbank.ctv.orderservice.sagas.SagaManagement.handle(com.mbbank.ctv.messages.events.OrderShippedEvent)
    end
end box
participant EndLifeCycle
SagaOrder -> aggregate_order --++: 12: Lưu thông tin \n state UpdateOrderStatusCommand
' com.mbbank.ctv.orderservice.aggregates.OrderAggregate.on(com.mbbank.ctv.messages.commands.UpdateOrderStatusCommand)
par
    aggregate_order -> aggregate_order --++: 13.1 lưu state \n OrderUpdatedEvent
    ' com.mbbank.ctv.orderservice.aggregates.OrderAggregate.on(com.mbbank.ctv.messages.events.OrderUpdatedEvent)
    aggregate_order -> OrderHandler --++: 13.2.1 update database \n order status
    ' com.mbbank.ctv.query.OrderHandler.on(com.mbbank.ctv.messages.events.OrderUpdatedEvent)
    OrderHandler -> repo_order --++: 13.2.2: repository update db
    aggregate_order -> SagaOrder --++: 13.3:  Handler event OrderUpdatedEvent
    ' com.mbbank.ctv.orderservice.sagas.SagaManagement.handle(com.mbbank.ctv.messages.events.OrderUpdatedEvent)
end
SagaOrder -> EndLifeCycle --: Kết thúc luồng
```

## II. Rollback flow
> UnitOfWork is a concept in Axon, In Axon, the Unit of Work is managed behind the scenes when you work with the framework.
> You don't typically interact directly with the Unit of Work instance, but you can still understand its role in managing 
> transactions and event processing.
> the Unit of Work is being managed by Axon behind the scenes as you interact with the framework's components, such as the CommandGateway.
> The framework ensures that the command handling, event sourcing, and event dispatching occur within the scope of a Unit of Work. 
> This allows for transactional consistency and event-driven communication between aggregates and other components.