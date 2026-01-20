package server;

import com.google.gson.GsonBuilder;
import dto.RequestDTO;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import dto.ResponseDTO;

public class MyServer {
    public static void main(String[] args) {
        try {
            // 1. 20000번 포트로 대기중....
            ServerSocket ss = new ServerSocket(20000);
            Socket socket = ss.accept(); // client가 연결될 때 까지 기다리다가 연결되면 새 소켓 만들어진다.

            // 2. 새로운 소켓에 버퍼 달기 (BR, BW)
            // BR
            InputStream in = socket.getInputStream();
            InputStreamReader ir = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(ir);
            // BW
            OutputStream out = socket.getOutputStream();
            OutputStreamWriter ow = new OutputStreamWriter(out);
            BufferedWriter bw = new BufferedWriter(ow);

            Gson gson = new GsonBuilder().serializeNulls().create();

            ProductService productService = new ProductService();

            while(true){
                // 1. client로 부터 받은 메시지(json 문자열로 들어 있다.)
                String line = br.readLine(); // 엔터키까지 읽기
                System.out.println("[server]" + line);

                // 2. 파싱 (json string -> object)
                RequestDTO requestDTO = gson.fromJson(line, RequestDTO.class);
                // id 추출
                Map<String, Integer> req = requestDTO.getQuerystring();
                // get 요청인지 판별 -> method 확인
                System.out.println("method : "+requestDTO.getMethod());

                // 3. 서비스 호출 (상품상세) if로 구현
                // Service 호출 → ResponseDTO 생성 상품상세(findById)
                if("get".equals(requestDTO.getMethod())
                        && requestDTO.getQuerystring() != null
                        && requestDTO.getQuerystring().containsKey("id")
                        && requestDTO.getBody() == null
                ){
                    Integer id = requestDTO.getQuerystring().get("id");
                    System.out.println("상품 상세 요청, id = "+ id);

                    ResponseDTO<Product> res;

                    try{
                        Product product = productService.상품상세(id);
                        res = new ResponseDTO<>("ok", product);
                    } catch (Exception e) {
                        res= new ResponseDTO<>(e.getMessage(), null);
                    }
                    // 4. 응답
                    String resJson = gson.toJson(res); // 1. res 객체를 JSON 문자열로 변환
                    bw.write(resJson + "\n");      // 2. 버퍼에 쓰고 줄바꿈 추가
                    bw.flush();                       // 3. 즉시 전송
                    System.out.println("[server] 응답 보냄: " + resJson );
                    //bw.write("ok");
                }
                // Service 호출 → RequestDTO 생성(findAll)
                else if("get".equals(requestDTO.getMethod())
                        && requestDTO.getQuerystring() == null
                        && requestDTO.getBody() == null)
                {
                    ResponseDTO<List<Product>> res;
                    try{
                        List<Product> list = productService.상품목록();
                        res = new ResponseDTO<>("ok", list);
                    } catch (Exception e) {
                        res = new ResponseDTO<>(e.getMessage(), null);
                    }
                    String resJson = gson.toJson(res);
                    bw.write(resJson + "\n");
                    bw.flush();
                }

                else if("post".equals(requestDTO.getMethod())
                        && requestDTO.getBody() != null) {
                    Map<String, Object> body = requestDTO.getBody();

                    String name = (String) body.get("name");
                    Integer price = ((Number) body.get("price")).intValue();

                    Integer qty = ((Number) body.get("qty")).intValue();


                    ResponseDTO<Void> res;
                    try {
                        productService.상품등록(name, price, qty);
                        res = new ResponseDTO<>("ok", null);
                    } catch (Exception e) {
                        res = new ResponseDTO<>(e.getMessage(), null);
                    }

                    String resJson = gson.toJson(res);
                    bw.write(resJson + "\n");
                    bw.flush();
                }

                // Service 호출 → RequestDTO 생성(deleteById)
                else if("delete".equals(requestDTO.getMethod())
                    && requestDTO.getQuerystring() != null
                    && requestDTO.getQuerystring().containsKey("id")) {
                    Integer id = requestDTO.getQuerystring().get("id");

                    ResponseDTO<Void> res;

                    try{
                        productService.상품삭제(id);
                        res = new ResponseDTO<>("ok", null);
                    } catch (Exception e) {
                        res = new ResponseDTO<>(e.getMessage(), null);
                    }
                    String resJson = gson.toJson(res);
                    bw.write(resJson + "\n");
                    bw.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}