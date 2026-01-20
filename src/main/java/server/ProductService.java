package server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;


public class ProductService implements ProductServiceInterface {

    //private Map<Integer, Product> productMap;

//    public ProductService(){
//        productMap = new HashMap<>();
//        productMap.put(1, new Product(1,"바나나",1000,10));
//        productMap.put(2, new Product(2,"사과",1500,5));
//    }

    private ProductRepository productRepository = new ProductRepository();

    public Product findById(int id) {
        // 2. 이제 맵이 아니라 DB에서 찾아옵니다.
        Product product = productRepository.findById(id);

        if (product == null) {
            throw new RuntimeException("id not found");
        }
        return product;
    }


    @Override
    public void 상품등록(String name, int price, int qty){
        productRepository.insert(name, price, qty);
    }

    @Override
    public List<Product> 상품목록() {
        List<Product> list = productRepository.findAll();
        if (list == null){
            throw new RuntimeException("상품이 없습니다.");
        }
        return list;
    }

    @Override
    public Product 상품상세(int id) {
        Product product = productRepository.findById(id);
        if (product == null) {
            throw new RuntimeException("상품을 찾을 수 없습니다.");
        }
        return product;
    }

    @Override
    public void 상품삭제(int id) {
        productRepository.deleteById(id);
    }
}
