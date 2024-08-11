package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Item.Book;
import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class OrderServiceTest {

  @Autowired
  EntityManager em;
  @Autowired
  OrderService orderService;
  @Autowired
  OrderRepository orderRepository;


  @Test
  public void 상품주문() throws Exception {

    Member member = createMember();
    Book book = createBook("시골 JPA", 10000, 10);

    int orderCount = 2;

    Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

    Order getOrder = orderRepository.findeOne(orderId);

    assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문시 상태는 ORDER");
    assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야 한다.");
    assertEquals(10000* orderCount, getOrder.getTotalPrice(),"주문 가격은 가격 * 상품 수량이다.");
    assertEquals(8, book.getStockQuantity(), "주문 수량만큼 재고가 줄어야 합니다.");
  }

  @Test
  public void 상품주문_재고수량초과() {
    Member member = createMember();
    Item item = createBook("시골 JPA", 10000, 10);

    int orderCount = 11;

    Assertions.assertThrows(NotEnoughStockException.class, () -> {
      orderService.order(member.getId(), item.getId(), orderCount);
    });
  }


  @Test
  public void 주문취소() throws Exception {
    Member member = createMember();
    Book item = createBook("사골 JPA", 10000, 10);
    int orderCount = 2;
    Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
    orderService.cancelOrder(orderId);

    Order getOrder = orderRepository.findeOne(orderId);
    assertEquals(OrderStatus.CANCEL, getOrder.getStatus(), "주문 취소시 상태는 CANCEL 이다.");
    assertEquals(10, item.getStockQuantity(), "주문이 취소된 상품은 그만큼 재고가 증가해야 합니다.");
  }

  private Book createBook(String name, int price, int stockQuantity){
    Book book = new Book();
    book.setName(name);
    book.setPrice(price);
    book.setStockQuantity(stockQuantity);

    em.persist(book);

    return book;
  }
  private Member createMember(){
    Member member = new Member();
    member.setName("회회원원님님");
    member.setAddress(new Address("서울","경기","123-155"));
    em.persist(member);

    return member;
  }
}