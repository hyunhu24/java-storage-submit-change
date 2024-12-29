package com.choongang;  // 패키지 선언, 해당 클래스를 com.choongang 패키지에 속하게 합니다.

import java.util.Scanner;  // Scanner 클래스를 사용하기 위해 java.util 패키지에서 가져옵니다.

public class MyStorage {
    public final static String EMPTY = "등록 가능";
    static String[] products = new String[]{EMPTY,EMPTY,EMPTY,EMPTY,EMPTY};
    static int[] counts = new int[]{0, 0, 0, 0, 0};

    public static void showMenu(){
        System.out.println("1. 물건 정보(제품명) 등록하기");
        System.out.println("2. 물건 정보(제품명) 등록 취소하기");
        System.out.println("3. 물건 넣기 (제품 입고)");
        System.out.println("4. 물건 빼기 (제품 출고)");
        System.out.println("5. 재고 조회");
        System.out.println("6. 프로그램 종료");
        System.out.println("-".repeat(30));  // '-' 문자를 30번 반복하여 줄을 그어 메뉴 구분을 명확하게 합니다.
    }

    public static int selectMenu(Scanner s){
        while (true) {
            int select = getValidNumber(s, "[System] 원하는 기능의 번호를 입력하세요 : ");
            if (select >= 1 && select <= 6) {
                return select;
            } else {
                System.out.println("해당하는 기능의 번호가 없습니다.");
            }
        }
    }

    // 제품 등록 로직을 구현합니다. 제품명을 입력받아 빈 배열 위치에 저장합니다.
    static void prod_input(Scanner s){
        // TODO:
        System.out.println("[System] 제품 등록을 원하는 제품명을 입력하세요 : ");

        String input = s.nextLine();
        int productIdx = -1;

        for (int i = 0; i < products.length; i++) {
            if (products[i] != null && products[i].equals(input)) {
                System.out.println("이미 해당하는 제품명이 있습니다.");
                return;
            }
            if (EMPTY.equals(products[i])) {
                productIdx = i;
                break;
            }
        }

        if (productIdx == -1) {
            System.out.println("제품 목록이 가득 찼습니다.");
            return;
        }

        products[productIdx] = input;
        counts[productIdx] = 0;

        System.out.println("[System] 등록이 완료되었습니다.");
        prod_search();
        System.out.println("-".repeat(30));
    }

    // 제품 등록 취소 로직을 구현합니다.
    static void prod_remove(Scanner s){
        System.out.println("[System] 제품 등록 취소를 원하는 제품명을 입력하세요 : ");
        String input = s.nextLine();

        int productIdx = findProductIndex(input);

        if(productIdx < 0){
            System.out.println("해당하는 제품이 등록되어 있지 않습니다.");
            return;
        }
        products[productIdx] = EMPTY;
        counts[productIdx] = 0;

        System.out.println("[System] 제품 취소가 완료됬습니다.");
        System.out.println("-".repeat(30));
    }

    public static void updateProductQuantity(Scanner s, boolean isAdding) {
        String action = isAdding ? "추가" : "출고";
        System.out.println("[System] 물건의 수량을 " + action + "합니다.");
        System.out.print("[System] 수량을 " + action + "할 제품명을 입력하세요 : ");
        String input = s.nextLine();
        int foundIdx = findProductIndex(input);

        if (foundIdx < 0) {
            System.out.println("[Warning] 입력한 제품명이 없습니다. 다시 확인하여 주세요.");
            return;
        }

        int cnt = getValidNumber(s, "[System] " + action + "할 수량을 입력해주세요 : ");

        if (!isAdding && cnt > counts[foundIdx]) {
            System.out.println("[Error] 남은 수량보다 더 많이 출고할 수 없습니다.");
            return;
        }

        counts[foundIdx] += isAdding ? cnt : -cnt;
        System.out.println("[Clear] 정상적으로 제품의 수량 " + action + "가 완료되었습니다.");
    }

    public static void prod_amount_add(Scanner s) {
        updateProductQuantity(s, true);
    }

    public static void prod_amount_decrease(Scanner s) {
        prod_search();
        updateProductQuantity(s, false);
    }

    static void prod_search(){
        System.out.println("[System] 현재 등록된 물건 목록 ▼");
        for (int i = 0; i < products.length; ++i){
            System.out.println("> " + products[i] + " : " + counts[i] + "개");
        }
    }

//제품 검색 로직 중복 제거
    private static int findProductIndex(String productName) {
        for (int i = 0; i < products.length; i++) {
            if (productName.equals(products[i])) {
                return i;
            }
        }
        return -1;
    }

    //숫자 유효성 검사
    private static boolean isValidNumber(String formula){
        String digits = "0123456789";
        for(char c : formula.toCharArray()){
            if(digits.indexOf(c) == -1){
                return false;
            }
        }
        return true;
    }

    public static int getValidNumber(Scanner s, String message) {
        while(true){
            System.out.println(message);
            String input = s.nextLine();
            if(isValidNumber(input)){
                return Integer.parseInt(input);
            }else{
                System.out.println("[Error] 유효한 숫자를 입력해주세요.");
            }
        }
    }

    public static void main(String[] args){
        System.out.println("[Item_Storage]");
        System.out.printf("[System] 점장님 어서오세요.\n[System] 해당 프로그램의 기능입니다.\n");

        Scanner s = new Scanner(System.in);
        while (true){
            showMenu();
            int menu = selectMenu(s);  // 메뉴 선택을 받습니다.
            if(menu == 6){  // '프로그램 종료' 선택 시
                System.out.println("[System] 프로그램을 종료합니다.");
                break;  // 반복문을 종료하고 프로그램을 종료합니다.
            }
            // 선택된 메뉴에 따라 해당 기능을 실행합니다.
            switch (menu){
                case 1:
                    prod_input(s);
                    break;
                case 2:
                    prod_remove(s);
                    break;
                case 3:
                    prod_amount_add(s);
                    break;
                case 4:
                    prod_amount_decrease(s);
                    break;
                case 5:
                    prod_search();
                    break;
                default:
                    System.out.println("[System] 시스템 번호를 다시 확인하여 주세요.");
            }
        }
        s.close();  // Scanner 객체를 닫습니다.
    }
}
