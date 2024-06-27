package com.kys.sbtest.make;

import com.kys.sbtest.EPhoneGroup;
import com.kys.sbtest.IPhoneBook;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneBook implements IPhoneBook {
    private Long id;
    private String name;
    private EPhoneGroup group;
    private String phoneNumber;
    private String email;

    @Override
    public String toString() {
        return String.format("ID:%6d, 이름:%s, 그룹:%s, 번호:%s, 이메일:%s"
                , this.id, this.name, this.group, this.phoneNumber, this.email);
    }
}
