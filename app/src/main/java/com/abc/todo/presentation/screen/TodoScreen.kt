package com.abc.todo.presentation.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FilterListOff
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.abc.todo.domain.model.Todo
import com.abc.todo.presentation.state.TodoUiState
import com.abc.todo.presentation.viewmodel.TodoViewModel
import com.abc.todo.ui.components.AppConfirmDialog
import com.abc.todo.utils.DateUtils.toFormattedDD
import com.abc.todo.utils.DateUtils.toFormattedDate
import com.abc.todo.utils.DateUtils.toFormattedMMM
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.Boolean

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(
    modifier: Modifier = Modifier,
    viewModel: TodoViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val isFiltered by viewModel.isFiltered.collectAsState()
    var sheetMode by remember { mutableStateOf<TodoSheetMode?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    
    // Date Range Picker State
    var showDateRangePicker by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "My Tasks",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent, // Transparent for clean look
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                ),
                windowInsets = WindowInsets(0, 0, 0, 0)
            )
        },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                SmallFloatingActionButton(
                    onClick = {
                        if (isFiltered) {
                            viewModel.toggleFilter() // Just turn it off
                        } else {
                            showDateRangePicker = true
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(
                        imageVector = if (isFiltered) Icons.Default.FilterListOff else Icons.Default.FilterList,
                        contentDescription = if (isFiltered) "Show All" else "Filter Date Range"
                    )
                }

                FloatingActionButton(
                    onClick = { sheetMode = TodoSheetMode.Add },
                    containerColor = Color(0xFF6200EE), // Vibrant Purple
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Todo")
                }
            }
        }
    ) { padding ->

        Box(modifier = Modifier.padding(padding)) {
            when (state) {
                is TodoUiState.Loading ->
                    CircularProgressIndicator(color = Color(0xFF6200EE))

                is TodoUiState.Success -> {
                    val todos = (state as TodoUiState.Success).todos
                    TodoList(
                        todos = todos,
                        onEdit = { todo -> sheetMode = TodoSheetMode.Edit(todo) },
                        onDelete = {todo -> viewModel.delete(todo) },
                        onToggle = { todo -> viewModel.toggle(todo) }
                    )
                }

                is TodoUiState.Error ->
                    Text("Something went wrong")
            }
        }
    }


    if (showDateRangePicker) {
        DateRangePickerModal(
            onDateRangeSelected = { startDate, endDate ->
                viewModel.setDateRange(startDate, endDate)
            },
            onDismiss = { showDateRangePicker = false }
        )
    }

    sheetMode?.let { mode ->
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { sheetMode = null }
        ) {
            TodoBottomSheet(
                mode = mode,
                onSubmit = { title, desc ,date->
                    when (mode) {
                        is TodoSheetMode.Add ->
                            viewModel.add(title, desc,date)

                        is TodoSheetMode.Edit ->
                            viewModel.update(
                                mode.todo.copy(
                                    title = title,
                                    description = desc,
                                    date = date
                                )
                            )
                    }
                    sheetMode = null
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerModal(
    onDateRangeSelected: (Long, Long) -> Unit,
    onDismiss: () -> Unit
) {
    val dateRangePickerState = rememberDateRangePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val start = dateRangePickerState.selectedStartDateMillis
                    val end = dateRangePickerState.selectedEndDateMillis
                    if (start != null && end != null) {
                        onDateRangeSelected(start, end)
                        onDismiss()
                    }
                },
                enabled = dateRangePickerState.selectedStartDateMillis != null && dateRangePickerState.selectedEndDateMillis != null
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DateRangePicker(
            state = dateRangePickerState,
            title = {
                Text(
                    text = "Select Date Range",
                    modifier = Modifier.padding(16.dp)
                )
            },
            showModeToggle = false,
            modifier = Modifier.weight(1f)
        )
    }
}


@Composable
fun TodoList(
    modifier: Modifier = Modifier,
    todos: List<Todo>,
    onEdit: (Todo) -> Unit,
    onDelete: (Todo) -> Unit,
    onToggle: (Todo) -> Unit
) {
    if (todos.isEmpty())
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No tasks yet", color = Color.Gray)
        }
    else
        LazyColumn(
            modifier = modifier.padding(horizontal = 16.dp), // Added horizontal padding for the whole list
            contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 144.dp) // Padding for FAB + Small FAB
        ) {
            items(
                items = todos,
                key = { todo -> todo.id }
            ) { todo ->
                SwipeTodoItem(
                    todo = todo,
                    onEdit = { onEdit(todo) },
                    onDelete = { onDelete(todo) },
                    onToggle = { onToggle(todo) }
                )
                Spacer(modifier = Modifier.size(12.dp)) // Spacing between items
            }
        }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodoCardView(
    modifier: Modifier = Modifier,
    todo: Todo,
    onEdit: () -> Unit,
    onToggle: () -> Unit
) {
    var showCompleteDialog by remember { mutableStateOf(false) }

    if (showCompleteDialog) {
        AppConfirmDialog(
            show = showCompleteDialog,
            title= "Complete Task",
            message= "Mark this task as completed?",
            confirmText = "Yes",
            dismissText = "Cancel",
            onConfirm={
                onToggle()
                showCompleteDialog = false },
            onDismiss={
                showCompleteDialog = false
            },
            confirmColor = Color(0xFF6200EE) // Purple
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {},
                onLongClick = {
                    // Disable long click (edit) if completed
                    if (!todo.isCompleted) {
                        onEdit()
                    }
                }
            ),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ){
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Date Column
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Text(
                    text = todo.date.toFormattedMMM().uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    fontSize = 10.sp
                )
                Text(
                    text = todo.date.toFormattedDD(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6200EE) // Purple
                )
            }
            
            // Divider line
            Box(
                modifier = Modifier
                    .size(width = 1.dp, height = 40.dp)
                    .background(Color.LightGray)
            )

            Column(modifier = Modifier.padding(start = 16.dp).weight(1f)) {
                Text(
                    text = todo.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (todo.isCompleted) Color.Gray else Color.Black,
                    maxLines = 1
                )

                if (todo.description.isNotBlank()) {
                    Text(
                        text = todo.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        maxLines = 1
                    )
                }
            }

            // Checkbox/Circle Icon
            IconButton(
                onClick = { 
                    if (!todo.isCompleted) showCompleteDialog = true else onToggle() 
                }
            ) {
                if (todo.isCompleted) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Completed",
                        tint = Color(0xFF6200EE), // Purple Check
                        modifier = Modifier.size(28.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.Circle, // Empty Circle
                        contentDescription = "Incomplete",
                        tint = Color.LightGray,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeTodoItem(
    modifier: Modifier = Modifier,
    todo: Todo,
    onEdit: (Todo) -> Unit,
    onDelete: (Todo) -> Unit,
    onToggle: (Todo) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {

        AppConfirmDialog(
            show = showDeleteDialog,
            title= "Delete Task",
            message= "Are you sure you want to delete this task?",
            confirmText = "Delete",
            dismissText = "Cancel",
            onConfirm={
                onDelete(todo)
                showDeleteDialog = false
            },
            onDismiss={
                showDeleteDialog = false
            },
            confirmColor = Color(0xFFD32F2F) // Red
        )
    }

    // Disable edit swipe if completed
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            when (value) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    if (todo.isCompleted) {
                        false // Disable edit if completed
                    } else {
                        onEdit(todo)
                        false // do NOT dismiss
                    }
                }
                SwipeToDismissBoxValue.EndToStart -> {
                    showDeleteDialog = true
                    false // do NOT dismiss immediately
                }
                else -> false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = !todo.isCompleted, // Disable swipe gesture itself visually
        backgroundContent = {
            SwipeBackground(dismissState)
        }
    ) {
        TodoCardView(
            modifier = modifier,
            todo = todo,
            onEdit = { onEdit(todo) },
            onToggle = { onToggle(todo) }
        )
    }

    LaunchedEffect(todo.id) {
        dismissState.reset()
    }
}



@Composable
fun SwipeBackground(state: SwipeToDismissBoxState) {
    val direction = state.dismissDirection

    val color = when (direction) {
        SwipeToDismissBoxValue.StartToEnd -> Color(0xFF6200EE) // Edit (Purple)
        SwipeToDismissBoxValue.EndToStart -> Color(0xFFD32F2F) // Delete (Red)
        else -> Color.Transparent
    }

    val icon = when (direction) {
        SwipeToDismissBoxValue.StartToEnd -> Icons.Default.Edit
        SwipeToDismissBoxValue.EndToStart -> Icons.Default.Delete
        else -> null
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(12.dp)) // Clip background to match card shape
            .background(color)
            .padding(16.dp),
        contentAlignment = if (direction == SwipeToDismissBoxValue.StartToEnd)
            Alignment.CenterStart else Alignment.CenterEnd
    ) {
        icon?.let {
            Icon(it, contentDescription = null, tint = Color.White)
        }
    }
}
