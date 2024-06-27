package com.kys.sbtest.make;

import com.kys.sbtest.IPhoneBook;
import com.kys.sbtest.IPhoneBookRepository;

import java.util.List;

public class PBRedisRepository implements IPhoneBookRepository<IPhoneBook> {
    @Override
    public boolean saveData(List<IPhoneBook> listData) throws Exception {
        return false;
    }

    @Override
    public boolean loadData(List<IPhoneBook> listData) throws Exception {
        return false;
    }
}
