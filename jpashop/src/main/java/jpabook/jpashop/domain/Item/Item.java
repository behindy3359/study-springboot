package jpabook.jpashop.domain.Item;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="dtype")
public abstract class Item {

  @Id @GeneratedValue
  @Column(name="item_id")
  private Long id;

  private String name;

  private int price;

  private int stockQuantity;

  @ManyToMany(mappedBy = "items")
  private List<Category> categories = new ArrayList<>();

  public void addStock(int quantity){// 재고수량을 증가( 또는 변화 )
    this.stockQuantity += quantity;
  }

  public void removeStock(int quantity){
    int restStock = this.stockQuantity - quantity;
    if (restStock < 0){
      throw new NotEnoughStockException("need more stock, 상품의 재고는 0 미만이 될 수 없습니다.");
    }
    this.stockQuantity = restStock;
  }
}
