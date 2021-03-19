package hiber.dao;

import hiber.model.Car;
import hiber.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

   @Autowired
   private SessionFactory sessionFactory;

   @Override
   public void add(User user) {
      sessionFactory.getCurrentSession().save(user);
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<User> listUsers() {
      TypedQuery<User> query=sessionFactory.getCurrentSession().createQuery("from User");
      return query.getResultList();
   }

   @Override
   public User getUserByCar(String model, int series) {
      String HQL = "FROM User u INNER JOIN Car c on u.car_id = c.id WHERE c.model = :mod AND c.series = :ser";
      String HQL1 = "from Car where model = :car_mod and series =:car_ser";
      String HQL2 = "from User where car_id = :car_id";
      List <Car> cars = sessionFactory.getCurrentSession().createQuery(HQL1, Car.class)
              .setParameter("car_mod", model).setParameter("car_ser", series).getResultList();
      if (!cars.isEmpty()){
         Car car = cars.get(0);
         return sessionFactory.getCurrentSession().createQuery(HQL2, User.class)
                 .setParameter("car_id", car.getId()).uniqueResult();
      }
      System.out.println("Автомобиль с такими параметрами не найден");
      return null;
   }

}
