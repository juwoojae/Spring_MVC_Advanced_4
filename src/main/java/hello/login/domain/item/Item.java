package hello.login.domain.item;

import lombok.Data;

/**
 * 도메인 영역 - 화면 ui, 기술 인프라 등등의 영역은 제외한
 * 시스템이 구현해야하는 핵심 비즈니스 업무 영역을 말한다
 *
 * Web 은 domain 에 의존하지만 domain 은 web 을 모르도록 설계해야 한다
 * web 을 바꾸거나 확장해도, domain 과는 전혀 영향이 없어야한다는것이다
 */
@Data
public class Item {

    private Long id;
    private String itemName;
    private Integer price;
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}