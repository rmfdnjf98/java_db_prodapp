package client;

import dto.RequestDTO;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;


public class MyClient {
    public static void main(String[] args) {
        try {
            // 1. Socket 연결 완료
            Socket socket = new Socket("localhost", 20000);

            // 2. 키보드 입력 버퍼
            InputStream keyStream = System.in;
            InputStreamReader keyReader = new InputStreamReader(keyStream);
            BufferedReader keyBuf = new BufferedReader(keyReader);

            // 3. BW 버퍼
            OutputStream out = socket.getOutputStream();
            OutputStreamWriter ow = new OutputStreamWriter(out);
            BufferedWriter bw = new BufferedWriter(ow);

            // 4. BR 버퍼
            InputStream in = socket.getInputStream();
            InputStreamReader ir = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(ir);

            Gson gson = new Gson();
            Integer id = null;
            while(true){
                // 요청
                String keyboardData = keyBuf.readLine();
                // 종료 명령 - exit 처리
                if(keyboardData.equalsIgnoreCase("exit")){
                    bw.write("exit\n");
                    bw.flush();
                    break;
                }
                //입력 파싱
                String[] tokens = keyboardData.split(" ");
                String method = tokens[0];

                RequestDTO requestDTO = null;
                Map<String, Integer> querystring = null;

                // 상품 상세: get 1 상세보기 / 목록보기
                if(method.equals("get")) {
                    if(tokens.length == 2){
                        id = Integer.parseInt(tokens[1]); // 문자열을 숫자로 바꾸기
                        //System.out.println(id);

                        //DTO 생성
                        querystring = new HashMap<>();
                        querystring.put("id", id);

                        requestDTO = new RequestDTO(method, querystring, null);
                    } else { // 목록보기
                        requestDTO = new RequestDTO(method, null, null);
                    }
                }

                // 상품 삭제: delete 1
                else if(method.equals("delete")){
                    id = Integer.parseInt(tokens[1]);
                    querystring = new HashMap<>();
                    querystring.put("id", id);
                    requestDTO = new RequestDTO(method, querystring, null);
                }

                //Gson으로 JSON 변환
                String json = gson.toJson(requestDTO);
                bw.write(json);
                bw.write("\n");
                bw.flush();

                // 응답
                String line = br.readLine();
                System.out.println(line);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
