package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // 비영속
            Member member = new Member(102L, "LEE");

            // 영속
            em.persist(member); // 1차 캐시에 저장

            // member를 조회할 때 db에서 select 해오지 않고 1차 캐시에서 조회한다. (성능 향상)
            // 마찬가지로 데이터를 처음부터 db에서 select 해서 가져올때도 처음엔 select 쿼리문이 실행되지만 2번째부턴 처음에 가져온 값이 1차 캐시 되기 때문에 캐시에서 조회한다
            // 즉, select 쿼리문은 처음 한번만 실행된다. (JPA는 쿼리로 가져온 데이터를 항상 캐싱한다.) (동일성도 보장한다 == 비교값은 true)
            Member findMember = em.find(Member.class, 102L);
            System.out.println("findMember.id = " + findMember.getId());
            System.out.println("findMember.name = " + findMember.getName());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
