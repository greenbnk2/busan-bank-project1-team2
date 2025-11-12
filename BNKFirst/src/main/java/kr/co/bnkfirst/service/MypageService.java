package kr.co.bnkfirst.service;

import jakarta.transaction.Transactional;
import kr.co.bnkfirst.dto.DocumentDTO;
import kr.co.bnkfirst.dto.mypage.DealDTO;
import kr.co.bnkfirst.dto.UsersDTO;
import kr.co.bnkfirst.dto.product.PcontractDTO;
import kr.co.bnkfirst.mapper.MypageMapper;
import kr.co.bnkfirst.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MypageService{

    final UsersRepository usersRepository;
    final MypageMapper mypageMapper;

    public UsersDTO findById(String mid) {
        return mypageMapper.findById(mid);
    }

    public DealDTO findByDeal(String mid) {
        return mypageMapper.findByDeal(mid);
    }

    public List<PcontractDTO> findByContract(String mid) {
        return mypageMapper.findByContract(mid);
    }

    public int findByBalance(String mid) {
        return mypageMapper.findByBalance(mid);
    }

    public List<DocumentDTO> findByDocumentList(String mid) {
        return mypageMapper.findByDocumentList(mid);
    }

    public void registerDeal(String mid, int dbalance, String dwho){
        mypageMapper.registerDeal(mid,dbalance,dwho);
    }

    public void updateContract(int dbalance, String dwho){
        mypageMapper.updateContract(dbalance,dwho);
    }

    public PcontractDTO findByOneContract(String pacc) {
        return mypageMapper.findByOneContract(pacc);
    }

    @Transactional
    public void transfer(String mid, int dbalance, String dwho, String myAcc, String yourAcc) {
        registerDeal(mid, -dbalance, dwho);
        registerDeal(findByOneContract(yourAcc).getPcuid(), dbalance, findById(mid).getMname());
        updateContract(findByOneContract(myAcc).getPbalance() - dbalance, myAcc);
        updateContract(findByOneContract(yourAcc).getPbalance() + dbalance, yourAcc);
    }

    public List<DealDTO> findByDealList(String mid){
        return mypageMapper.findByDealList(mid);
    }

    public int findBySumPlusDbalance(String mid){
        return mypageMapper.findBySumPlusDbalance(mid);
    }

    public int findBySumMinusDbalance(String mid){
        return mypageMapper.findBySumMinusDbalance(mid);
    }

//    상품 해지 과정
    public void updateRecvContract(int pbalance, String pacc) {
        mypageMapper.updateRecvContract(pbalance, pacc);
    }

    public void deleteContract(String pacc) {
        mypageMapper.deleteContract(pacc);
    }

    @Transactional
    public void deleteContractProcess(int pbalance, String pacc, String recvAcc) {
        updateRecvContract(pbalance, recvAcc);
        deleteContract(pacc);
    }
}
