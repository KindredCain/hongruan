package servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hibernate.HibernateUtil;
import model.Worker;

@Controller
public class WorkerController {
	
	/*---------登录工人---------*/
	@RequestMapping("login.do")
	public String Login(@RequestParam(value="id")String id, @RequestParam(value="pwd")String pwd, HttpServletRequest request) {
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			if(id==null ||id.equals(""))
				throw new Exception("请输入用户名");
			if(pwd==null)
				pwd="";
			session.beginTransaction();
			Worker worker=(Worker) session.get(Worker.class, id);
			session.getTransaction().commit();
			if(worker==null) 
				throw new Exception("用户不存在");
			if(!pwd.equals(worker.getPwd()))
				throw new Exception("密码错误");
			request.getSession().setAttribute("ID",worker.getId());
			if(worker.getType().equals("admin"))
				return "admin";
			else if(worker.getType().equals("mod"))
				return "redirect:/selectnextpicture.do";
			else if(worker.getType().equals("check"))
				return "redirect:/selectnextpicturecheck.do";
			else
				return "login";
		} catch (Exception ex) {
			request.getSession().setAttribute("errormsg", ex.getMessage());
			return "login";
		}
	}
	
	/*----------修改职务----------*/
	@RequestMapping("modtype.do")
	public String ModType(@RequestParam(value="id")String id, @RequestParam(value="type")String type, HttpServletRequest request) {
		try {
			if(type.equals("0"))
				throw new Exception("请选择职务");
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Worker worker = (Worker) session.get(Worker.class, id);
			if(worker == null) 
				throw new Exception("用户不存在");
			worker.setType(type);
			session.update(worker);
			session.getTransaction().commit();
			return "admin";
		} catch (Exception ex) {
			request.getSession().setAttribute("errormsg", ex.getMessage());
			return "login";
		}
	}
	
	/*----------查询全部----------*/
	@RequestMapping("selectworker.do")
	public String SelectWorker(HttpServletRequest request) {
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			String hql = "from Worker";  
	        Query query = session.createQuery(hql);  
	        List<Worker> workerlist = query.list(); 
			session.getTransaction().commit();
			request.setAttribute("objlist", workerlist);
			request.setAttribute("cname", "worker");
			return "admin";
		} catch (Exception ex) {
			request.getSession().setAttribute("errormsg", ex.getMessage());
			return "login";
		}
	}
	
	/*----------职位查询----------*/
	@RequestMapping("selecttype.do")
	public String SelectType(@RequestParam(value="type")String type, HttpServletRequest request) {
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			String hql = "from Worker where type=?";
	        Query query = session.createQuery(hql);
	        query.setString(0, type);
	        List<Worker> workerlist = query.list(); 
			session.getTransaction().commit();
			request.setAttribute("objlist", workerlist);
			return "success";
		} catch (Exception ex) {
			request.getSession().setAttribute("errormsg", ex.getMessage());
			return "login";
		}
	}
	
	/*----------根据id查询----------*/
	@RequestMapping("selectworkerid.do")
	public String SelectWorkerId(@RequestParam(value="id")String id, HttpServletRequest request) {
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			String hql = "from Worker where id=?";  
	        Query query = session.createQuery(hql);  
	        query.setString(0, id);
	        List<Worker> workerlist = query.list(); 
			session.getTransaction().commit();
			request.setAttribute("objlist", workerlist);
			request.setAttribute("cname", "worker");
			return "admin";
		} catch (Exception ex) {
			request.getSession().setAttribute("errormsg", ex.getMessage());
			return "login";
		}
	}
	
	/*---------登出---------*/
	@RequestMapping("out.do")
	public String Out(HttpServletRequest request) {
		request.getSession().invalidate();
        return "login";
	}
	
}
