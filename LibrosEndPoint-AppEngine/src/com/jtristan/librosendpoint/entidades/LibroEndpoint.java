package com.jtristan.librosendpoint.entidades;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.users.User;
import com.google.appengine.datanucleus.query.JDOCursorHelper;
import com.jtristan.librosendpoint.IDs;

@Api(name = "libroendpoint", namespace = @ApiNamespace(ownerDomain = "jtristan.com", ownerName = "jtristan.com", packagePath = "librosendpoint.entidades"))
public class LibroEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listLibro")
	public CollectionResponse<Libro> listLibro(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<Libro> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(Libro.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<Libro>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Libro obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Libro> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getLibro")
	public Libro getLibro(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		Libro libro = null;
		try {
			libro = mgr.getObjectById(Libro.class, id);
		} finally {
			mgr.close();
		}
		return libro;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param libro the entity to be inserted.
	 * @return The inserted entity.
	 */	
	@ApiMethod(name = "insertLibro",
	clientIds={IDs.CLIENT_ID,
				com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID,
				IDs.AUDIENCE},
	audiences={IDs.AUDIENCE})
	public Libro insertLibro1(Libro libro, User usuario) {	
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (containsLibro(libro)) {
				throw new EntityExistsException("Object already exists");
			}
			libro.setUsuario(usuario);
			mgr.makePersistent(libro);
		} finally {
			mgr.close();
		}
		return libro;
	}
	


	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param libro the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateLibro")
	public Libro updateLibro(Libro libro) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsLibro(libro)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(libro);
		} finally {
			mgr.close();
		}
		return libro;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeLibro")
	public void removeLibro(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			Libro libro = mgr.getObjectById(Libro.class, id);
			mgr.deletePersistent(libro);
		} finally {
			mgr.close();
		}
	}

	private boolean containsLibro(Libro libro) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(Libro.class, libro.getId());
		} catch (javax.jdo.JDOObjectNotFoundException ex) {
			contains = false;
		} finally {
			mgr.close();
		}
		return contains;
	}

	private static PersistenceManager getPersistenceManager() {
		return PMF.get().getPersistenceManager();
	}

}
