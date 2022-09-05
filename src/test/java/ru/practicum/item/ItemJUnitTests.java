package ru.practicum.item;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.booking.BookingJpaRepository;
import ru.practicum.common.Mapper;
import ru.practicum.user.User;
import ru.practicum.user.UserDTO;
import ru.practicum.user.UserJpaRepository;

@ExtendWith(MockitoExtension.class)
public class ItemJUnitTests {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserJpaRepository userRepository;
    @Mock
    private BookingJpaRepository bookingRepository;
    @Mock
    Mapper mapper;
//    @Mock
//    private CommentRepository commentRepository;
//    @Mock
//    private CommentDtoMapper commentDtoMapper;
//    @Mock
//    private BookingDtoMapper bookingDtoMapper;


    User user = new User(1L, "James", "james@yandex.com");
    UserDTO userDTO = new UserDTO(1L, "James", "james@yandex.com");

    Item item1 = Item.builder().name("1")
            .description("отверТка")
            .available(true)
            .ownerId(userDTO.getId())
            .build();
    Item item2 = Item.builder().name("2")
            .description("молоток")
            .available(true)
            .ownerId(userDTO.getId())
            .build();

    @InjectMocks
    private ItemService itemService;

//    @Test
//    public void shouldFailGetAllOnWrongOwnerId() throws NotExistedUserException {
//        when(itemRepository.getAllUserItems(anyLong()))
//                .thenThrow(NotExistedUserException.class);
//
//        Assertions.assertThrows(NotExistedUserException.class, () -> {
//            itemService.getAllUserItems(1, 0, 5);
//        });
//    }


}