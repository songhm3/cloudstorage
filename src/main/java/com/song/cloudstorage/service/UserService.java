package com.song.cloudstorage.service;


import com.song.cloudstorage.dao.MyDiskInfoDao;
import com.song.cloudstorage.dao.MyFileDao;
import com.song.cloudstorage.dao.UserDao;
import com.song.cloudstorage.model.MyDiskInfo;
import com.song.cloudstorage.model.MyFile;
import com.song.cloudstorage.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Transactional(readOnly = true, rollbackFor = {Exception.class})
public class UserService extends BaseService{

    private static final long DEFAULT_TOTAL_SIZE = 1024 * 1024 * 5;

    @Autowired
    private UserDao userDao;

    @Autowired
    private MyDiskInfoDao myDiskInfoDao;

    @Autowired
    private MyFileDao myFileDao;

   

	public String confirmEmail(String email) {
		// TODO Auto-generated method stub
		if(userDao.countUsersByEmail(email) > 0){
			return "0";
		}
		
		return "1";
	}

	public String confirmUsername(String username) {
		// TODO Auto-generated method stub
		if(userDao.countUsersByUsername(username) > 0){
			return "0";
		}
		return "1";
	}


	public boolean login(User user) {
		// TODO Auto-generated method stub
		User u = userDao.getUserByEmailAndPassword(user);
        if(u != null) {
            session.setAttribute("user", u);
            session.setAttribute("diskInfo", myDiskInfoDao.getMyDiskInfoById(u.getId()));
            session.setAttribute("homeId", myFileDao.getMyFileByName("#" + u.getId()).getId());
            return true;
        }
		return false;
	}

	public boolean loginConfirm(User user) {
		// TODO Auto-generated method stub
		User u = userDao.getUserByEmailAndPassword(user);
    	if(u == null) return false;
    	session.setAttribute("user", u);
		return true;
	}

	public void logout(User user) {
		// TODO Auto-generated method stub
		session.invalidate();
	}

	@Transactional(readOnly = false)
	public void register(User user) {
		// TODO Auto-generated method stub
		//saveUser;
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy (E)");
        user.setJoindate(sdf.format(new Date()));
        userDao.insertUser(user);
        int uID = user.getId();
        MyDiskInfo myDiskInfo = new MyDiskInfo();
        myDiskInfo.setUser_id(uID);
        myDiskInfo.setTotalSize(DEFAULT_TOTAL_SIZE);;
        myDiskInfoDao.insertMyDiskInfo(myDiskInfo);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		
        MyFile myFile = new MyFile();
        myFile.setCreateDate(sdf1.format(new Date()));
        myFile.setUser_id(uID);
        myFile.setName("#"+uID);
        myFile.setPath("/");
        myFileDao.insertMyFile(myFile);
        int homeID = myFile.getId();
        String [] folders = {"Documents", "Music", "Photos", "Books"};
        for(int i = 0; i < 4; i++) {
        	MyFile tempFile = new MyFile();
        	tempFile.setCreateDate(sdf1.format(new Date()));
        	tempFile.setUser_id(uID);
        	tempFile.setName(folders[i]);
        	tempFile.setParent_id(homeID);
        	tempFile.setPath("/"+homeID+"/");
            myFileDao.insertMyFile(tempFile);
        }
        user.setPortrait("portrait");
		
		
		
		
//		System.out.println("user id: "+user.getId());
//		System.out.println("diskinfo id: "+myDiskInfoDao.getMyDiskInfoById(user.getId()).getId());
//		System.out.println("diskinfo filenumber: "+myDiskInfoDao.getMyDiskInfoById(user.getId()).getFileNumber());
//		System.out.println("diskinfo totalsize: "+myDiskInfoDao.getMyDiskInfoById(user.getId()).getTotalSize());
//		System.out.println("homeId: "+myFileDao.getMyFileByName("#" + user.getId()).getId());
        session.setAttribute("diskInfo", myDiskInfoDao.getMyDiskInfoById(user.getId()));
        session.setAttribute("homeId", myFileDao.getMyFileByName("#" + user.getId()).getId());
        session.setAttribute("user", user);
	}
}
