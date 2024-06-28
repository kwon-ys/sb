package com.kys.sbtest;

import com.kys.sbtest.make.PhoneBook;

import java.util.List;
import java.util.Scanner;

public class ConsoleApplication {
    private IPhoneBookService<IPhoneBook> phoneBookService;
    public void setPhoneBookService(IPhoneBookService<IPhoneBook> phoneBookService) throws Exception {
        this.phoneBookService = phoneBookService;
        this.phoneBookService.loadData();
    }

    public ConsoleApplication() {
    }

    public void printTitle() {
        System.out.println("============================================================================");
        System.out.println("1.연락처생성|2.목록|3.수정|4.삭제|5.이름검색|6.그룹검색|7.폰검색|8.이메일검색|9.종료");
        System.out.println("============================================================================");
    }

    public int getChoice(Scanner input) throws Exception {
        System.out.print("선택 > ");
        String a = input.nextLine();
        return Integer.parseInt(a);
    }

    public void printList() {
        this.printList(this.phoneBookService.getAllList());
    }

    private EPhoneGroup getGroupFromScanner(Scanner input, String title) {
        while (true) {
            System.out.print(title + "연락처 그룹 선택 (Friends(1), Families(2), Schools(3), Jobs(4), Hobbies(5)) : ");
            String group = input.nextLine().trim();

            if (group.isEmpty()) {
                return null; // 입력이 없으면 null 반환하여 이전 값 유지
            }

            switch (group) {
                case "1":
                    return EPhoneGroup.Friends;
                case "2":
                    return EPhoneGroup.Families;
                case "3":
                    return EPhoneGroup.Schools;
                case "4":
                    return EPhoneGroup.Jobs;
                case "5":
                    return EPhoneGroup.Hobbies;
                default:
                    System.out.println("Friends(1), Families(2), Schools(3), Jobs(4), Hobbies(5) 중에서 입력해주세요.");
                    break;
            }
        }
    }

    public void insert(Scanner input) throws Exception {
        System.out.println("--------");
        System.out.println("연락처 생성");
        System.out.println("--------");

        String name;
        while (true) {
            System.out.print("연락처 이름 : ");
            name = input.nextLine();
            if (!name.isEmpty()) {
                break; // 이름이 비어있지 않으면 종료
            }
            System.out.println("이름을 적으세요");
        }

        EPhoneGroup group = null;
        while (group == null) {
            group = this.getGroupFromScanner(input, "");
            if (group == null) {
                System.out.println("연락처 그룹을 선택하세요.");
            }
        }

        String phone;
        while (true) {
            System.out.print("전화번호 : ");
            phone = input.nextLine();
            if (!phone.isEmpty()) {
                break; // 전화번호가 비어있지 않으면 종료
            }
            System.out.println("전화번호를 적으세요");
        }

        String email;
        while (true) {
            System.out.print("이메일 : ");
            email = input.nextLine();
            if (!email.isEmpty()) {
                break; // 이메일이 비어있지 않으면 종료
            }
            System.out.println("이메일을 적으세요");
        }

        if (this.phoneBookService.insert(name, group, phone, email)) {
            this.phoneBookService.saveData();
            System.out.println("결과: 데이터 추가 성공되었습니다.");
        }
    }


    public void update(Scanner input) throws Exception {
        IPhoneBook result = getFindIdConsole(input, "수정할");
        if (result == null) {
            System.out.println("에러: ID 데이터 가 존재하지 않습니다.");
            return;
        }

        System.out.print("연락처 이름 :");
        String name = input.nextLine();
        if (name.isEmpty()) {
            name = result.getName(); // 기존 이름 유지
        }

        EPhoneGroup group = this.getGroupFromScanner(input, "");
        if (group == null) {
            group = result.getGroup(); // 이전 그룹 유지
        }

        System.out.print("전화번호 :");
        String phone = input.nextLine();
        if (phone.isEmpty()) {
            phone = result.getPhoneNumber(); // 기존 전화번호 유지
        }

        System.out.print("이메일 :");
        String email = input.nextLine();
        if (email.isEmpty()) {
            email = result.getEmail(); // 기존 이메일 유지
        }

        IPhoneBook update = PhoneBook.builder()
                .id(result.getId()).name(name)
                .group(group)
                .phoneNumber(phone).email(email).build();
        if (this.phoneBookService.update(update.getId(), update)) {
            this.phoneBookService.saveData();
            System.out.println("결과: 데이터 수정 성공되었습니다.");
        }
    }

    public void delete(Scanner input) throws Exception {
        IPhoneBook result = getFindIdConsole(input, "삭제할");
        if (result == null) {
            System.out.println("에러: ID 데이터 가 존재하지 않습니다.");
            return;
        }
        if (this.phoneBookService.remove(result.getId())) {
            this.phoneBookService.saveData();
            System.out.println("결과: 데이터 삭제 성공되었습니다.");
        } else {
            System.out.println("결과: 데이터 삭제 실패되었습니다.");
        }
    }

    private IPhoneBook getFindIdConsole(Scanner input, String title) {
        long l = 0L;
        do {
            System.out.print(title + " ID 번호:");
            String id = input.nextLine();
            try {
                l = Long.parseLong(id);
            } catch (Exception ex) {
                System.out.println("ID 번호를 숫자로만 입력하세요.");
            }
        } while ( l <= 0 );
        IPhoneBook iPhoneBook = (IPhoneBook)this.phoneBookService.findById(l);
        return iPhoneBook;
    }

    private void printList(List<IPhoneBook> array) {
        for (IPhoneBook object : array) {
            System.out.println(object.toString());
        }
    }

    public void searchByName(Scanner input) {
        while (true) {
            System.out.print("찾을 이름 :");
            String findName = input.nextLine();
            if (findName.isEmpty()) {
                System.out.println("이름을 입력하세요");
                continue; // 이름이 비어 있으면 다시 입력 받기
            }

            List<IPhoneBook> list = this.phoneBookService.getListFromName(findName);
            if (list.isEmpty()) {
                System.out.println("저장된 이름이 없습니다.");
                return; // 저장된 값이 아닌 경우
            }
            this.printList(list);
            break;
        }
    }

    public void searchByGroup(Scanner input) {
        while (true) {
            EPhoneGroup group = this.getGroupFromScanner(input, "찾을 ");
            if (group == null) {
                System.out.println("연락처 그룹을 입력하세요.");
                continue; // 그룹이 비어 있으면 다시 입력 받기
            }

            List<IPhoneBook> list = this.phoneBookService.getListFromGroup(group);
            if (list.isEmpty()) {
                System.out.println("저장된 연락처 그룹이 없습니다.");
                return; // 저장된 값이 없는 경우
            }
            this.printList(list);
            break;
        }
    }

    public void searchByPhone(Scanner input) {
        while (true) {
            System.out.print("찾을 번호 :");
            String findPhone = input.nextLine();
            if (findPhone.isEmpty()) {
                System.out.println("번호를 입력하세요");
                continue; // 번호가 비어 있으면 다시 입력 받기
            }

            List<IPhoneBook> list = this.phoneBookService.getListFromPhoneNumber(findPhone);
            if (list.isEmpty()) {
                System.out.println("저장된 전화번호가 없습니다.");
                return; // 저장된 값이 없는 경우
            }
            this.printList(list);
            break;
        }
    }

    public void searchByEmail(Scanner input) {
        while (true) {
            System.out.print("찾을 Email :");
            String findEmail = input.nextLine();
            if (findEmail.isEmpty()) {
                System.out.println("이메일을 입력하세요");
                continue; // 이메일이 비어 있으면 다시 입력 받기
            }

            List<IPhoneBook> list = this.phoneBookService.getListFromEmail(findEmail);
            if (list.isEmpty()) {
                System.out.println("저장된 이메일이 없습니다.");
                return; // 저장된 값이 없는 경우
            }
            this.printList(list);
            break;
        }
    }
}