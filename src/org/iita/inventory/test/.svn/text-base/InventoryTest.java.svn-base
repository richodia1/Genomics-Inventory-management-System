/**
 * 
 */
package org.iita.inventory.test;

import java.util.List;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.iita.inventory.model.FieldVariables;
import org.iita.inventory.model.Lot;
import org.iita.inventory.remote.InventoryService;
import org.iita.inventory.remote.LotList;
import org.iita.inventory.service.LotService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

/**
 * @author KOraegbunam
 *
 */
public class InventoryTest extends
	AbstractTransactionalDataSourceSpringContextTests {

		protected LotService lotService;
		private ApplicationContext ctx;

		public InventoryTest() {
			super();
			ctx = new ClassPathXmlApplicationContext("/applicationContext-project.xml");
			lotService = (LotService) ctx.getBean("lotService");
		}
		
		@Test
		public final void testListLots() {
			System.out.println("------------------------------------------------");
			List<FieldVariables> fv = lotService.getLotFieldVariablesByLotId(212768L);

			assertNotNull(fv);
//assertEquals(212768L, lots.getId());
			for (FieldVariables fvlist : fv)
				System.out.println(fvlist.getLot().getBarCode());	
		}
}
