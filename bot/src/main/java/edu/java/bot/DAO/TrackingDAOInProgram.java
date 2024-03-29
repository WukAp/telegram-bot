package edu.java.bot.DAO;

import edu.java.bot.models.LinkModel;
import edu.java.bot.models.UserModel;
import edu.java.bot.models.UserStatus;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class TrackingDAOInProgram implements TrackingDao {

    private static final Logger LOGGER = LogManager.getLogger(TrackingDAOInProgram.class);
    private final Map<Long, UserModel> data = new HashMap<>();

    @Override
    public void addUser(long id) {
        data.put(id, new UserModel(id));
        LOGGER.info("был добавлен user с id = {}.", id);
    }

    @Override
    public UserModel getUser(long id) {
        return data.get(id);
    }

    @Override
    public void deleteUser(long id) {
        data.remove(id);
        LOGGER.info("был удалён user с id = {}.", id);
    }

    @Override
    public void addLink(long id, LinkModel link) {
        findUserByIdOrThrowException(id).addNewLink(link);
        LOGGER.info("была добавлена ссылка " + link + " для user'а с id = {}.", id);
    }

    @Override
    public void removeLink(long id, LinkModel link) {
        findUserByIdOrThrowException(id).removeLink(link);
        LOGGER.info("была удалена ссылка " + link + " у user'а с id = {}.", id);
    }

    @Override
    public UserStatus getUserStatus(long id) {
        UserStatus status;
        if (data.containsKey(id)) {
            status = data.get(id).getStatus();
        } else {
            status = UserStatus.UNREGISTRED;
        }
        LOGGER.info("У user'а с id = " + id + " был получен статус {}.", status);
        return status;
    }

    @Override
    public void setUserStatus(long id, UserStatus status) {
        findUserByIdOrThrowException(id).setStatus(status);
        LOGGER.info("был изменён статус user'а с id = {}. на {}.", id, status.name());
    }

    @Override
    public List<LinkModel> getLinks(long id) {
        List<LinkModel> links = findUserByIdOrThrowException(id).getLinksList();
        LOGGER.info("были найдены ссылки {}. user'а с id = {}, ", links, id);
        return links;
    }

    private UserModel findUserByIdOrThrowException(Long id) {
        if (data.containsKey(id)) {
            return data.get(id);
        }
        throw new UnknownUserException(id);
    }

    public static class UnknownUserException extends IllegalArgumentException {
        public UnknownUserException(Long id) {
            super("Can't find user with id = " + id);
        }
    }
}
